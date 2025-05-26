/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.client.loader.json.model.ModelFormatVersion;
import software.bluelib.client.loader.json.model.deserialize.Model;
import software.bluelib.client.loader.model.ModelLoader;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.loader.loading.json.typeadapter.BakedAnimationsAdapter;
import software.bluelib.loader.loading.object.BakedAnimations;
import software.bluelib.client.loader.json.model.ModelCacheFactory;
import software.bluelib.client.loader.json.model.object.BoneTree;
import software.bluelib.loader.util.CompoundException;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ResourceCache {
	public static final ResourceLocation RELOAD_LISTENER_ID = BlueLibCommon.Resource.resource("models_animations");
	public static final ResourceLocation ANIMATIONS_PATH = BlueLibCommon.Resource.resource("bluelib/animations");
	public static final ResourceLocation MODELS_PATH = BlueLibCommon.Resource.resource("models");
	public static final Pattern SUFFIX_STRIPPER = Pattern.compile("((\\.geo)|((\\.animation)s?))?(\\.json)$");
	public static final Pattern PREFIX_STRIPPER = Pattern.compile("^(bluelib/)((animations/)|(models/))?");

	private static Map<ResourceLocation, BakedAnimations> ANIMATIONS = Collections.emptyMap();
	private static Map<ResourceLocation, ModelCache> MODELS = Collections.emptyMap();

	public static Map<ResourceLocation, BakedAnimations> getBakedAnimations() {
		return ANIMATIONS;
	}

	public static Map<ResourceLocation, ModelCache> getBakedModels() {
		return MODELS;
	}

	public static void registerReloadListener() {
		Minecraft mc = Minecraft.getInstance();

		if (mc.getResourceManager() instanceof ReloadableResourceManager pResourceManager)
			pResourceManager.registerReloadListener(ResourceCache::reload);
	}

	private static CompletableFuture<Void> reload(PreparationBarrier pStage, ResourceManager pResourceManager, ProfilerFiller pProfilerFiller, ProfilerFiller pProfilerFiller1, Executor pBackgroundExecutor, Executor pGameExecutor) {
		CompletableFuture<Map<ResourceLocation, BakedAnimations>> animations = loadAnimations(pBackgroundExecutor, pResourceManager);
		CompletableFuture<Map<ResourceLocation, ModelCache>> models = loadModels(pBackgroundExecutor, pResourceManager);

		BaseLogger.log(BaseLogLevel.ERROR, "Models: " + models);
		
		return CompletableFuture.runAsync(() -> BakedAnimationsAdapter.COMPRESSION_CACHE = new ConcurrentHashMap<>(), pBackgroundExecutor)
				.thenCompose(ignored -> CompletableFuture.allOf(animations, models).thenCompose(pStage::wait).thenRunAsync(() -> {
					ResourceCache.ANIMATIONS = animations.join();
					ResourceCache.MODELS = models.join();
					BakedAnimationsAdapter.COMPRESSION_CACHE = null;
				}, pGameExecutor));

	}

	public static ResourceLocation stripPrefixAndSuffix(ResourceLocation pResourceLocation) {
		String newPath = pResourceLocation.getPath();
		Matcher prefixMatcher = PREFIX_STRIPPER.matcher(newPath);
		newPath = prefixMatcher.find() ? newPath.substring(prefixMatcher.end()) : newPath;
		Matcher suffixMatcher = SUFFIX_STRIPPER.matcher(newPath);
		newPath = suffixMatcher.find() ? newPath.substring(0, suffixMatcher.start()) : newPath;

		return newPath.length() == pResourceLocation.getPath().length() ? pResourceLocation : pResourceLocation.withPath(newPath);
	}

	private static CompletableFuture<Map<ResourceLocation, BakedAnimations>> loadAnimations(Executor pBackgroundExecutor, ResourceManager pResourceManager) {
		return bakeJsonResources(pBackgroundExecutor, pResourceManager, ANIMATIONS_PATH.getPath(), ResourceCache::bakeAnimations,
				ex -> new BakedAnimations(new Object2ObjectOpenHashMap<>()));
	}

	private static CompletableFuture<Map<ResourceLocation, ModelCache>> loadModels(Executor pBackgroundExecutor, ResourceManager pResourceManager) {
		return bakeJsonResources(pBackgroundExecutor, pResourceManager, MODELS_PATH.getPath(), ResourceCache::bakeModel,
				ex -> null);
	}

	private static <BAKED> CompletableFuture<Map<ResourceLocation, BAKED>> bakeJsonResources(Executor pBackgroundExecutor, ResourceManager pResourceManager, String pAssetPath,
	                                                                                         BiFunction<ResourceLocation, JsonObject, BAKED> pElementFactory, Function<Throwable, BAKED> pExceptionalFactory) {


		return loadResources(pBackgroundExecutor, pResourceManager, pAssetPath, "json", ResourceCache::readJsonFile)
				.thenCompose(resources -> {
					List<CompletableFuture<Pair<ResourceLocation, BAKED>>> tasks = new ObjectArrayList<>(resources.size());

					resources.forEach(pair -> tasks.add(CompletableFuture.supplyAsync(() -> Pair.of(stripPrefixAndSuffix(pair.left()), pElementFactory.apply(pair.left(), pair.right())), pBackgroundExecutor)
							.exceptionally(ex -> {
								ex.printStackTrace();

								return Pair.of(pair.left(), pExceptionalFactory.apply(ex));
							})));

					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
							.thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toMap(Pair::left, Pair::right)));
				});
	}

	private static <UNBAKED> CompletableFuture<List<Pair<ResourceLocation, UNBAKED>>> loadResources(Executor pExecutor, ResourceManager pResourceManager, String pAssetPath, String pFileType, BiFunction<ResourceLocation, Resource, UNBAKED> pElementFactory) {
		final String fileTypeSuffix = "." + pFileType;

		return CompletableFuture.supplyAsync(() -> pResourceManager.listResources(pAssetPath, fileName -> fileName.getPath().endsWith(fileTypeSuffix)), pExecutor)
				.thenCompose(resources -> {
					List<CompletableFuture<Pair<ResourceLocation, UNBAKED>>> tasks = new ObjectArrayList<>(resources.size());

					resources.forEach((path, resource) -> tasks.add(CompletableFuture.supplyAsync(() -> Pair.of(path, pElementFactory.apply(path, resource)), pExecutor)));

					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList());
				});
	}

	@NotNull
	private static ModelCache bakeModel(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
		if (pResourceLocation.getPath().endsWith(".animation.json"))
			throw new RuntimeException("Found animation file found in models folder! '" + pResourceLocation + "'");

		Model model = ModelLoader.MODEL_GSON.fromJson(pJsonObject, Model.class);
		ModelFormatVersion matchedVersion = ModelFormatVersion.match(model.modelFormatVersion());

		if (matchedVersion == null) {
			System.out.printf("%s: Unknown geo model format version: '%s'. This may not work correctly%n", pResourceLocation, model.modelFormatVersion());
		} else if (!matchedVersion.isSupported()) {
			System.out.printf("%s: Unsupported geo model format version: '%s'. %s%n", pResourceLocation, model.modelFormatVersion(), matchedVersion.getErrorMessage());
		}

		return ModelCacheFactory.getForNamespace(pResourceLocation.getNamespace()).constructGeoModel(BoneTree.fromModel(model));
	}

	@NotNull
	private static BakedAnimations bakeAnimations(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
		if (pResourceLocation.getPath().endsWith(".geo.json"))
			throw new RuntimeException("Found model file in animations folder! '" + pResourceLocation + "'");

		try {
			return ModelLoader.MODEL_GSON.fromJson(GsonHelper.getAsJsonObject(pJsonObject, "animations"), BakedAnimations.class);
		} catch (CompoundException ex) {
			throw ex.withMessage(pResourceLocation + ": Error building animations from JSON");
		} catch (Exception pException) {
			throw new RuntimeException(pResourceLocation + ": Error building animations from JSON", pException);
		}
	}

	private static JsonObject readJsonFile(ResourceLocation pResourceLocation, Resource pResource) {
		try (Reader reader = pResource.openAsReader()) {
			return GsonHelper.parse(reader);
		} catch (IOException pIoException) {
			throw new RuntimeException("Failed to read resource: " + pResourceLocation, pIoException);
		}
	}
}
