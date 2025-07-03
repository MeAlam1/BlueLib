/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.json.JSONMerger;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.loader.cache.animations.keyframe.KeyframeLibraryCache;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.cache.variants.EntityCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.animation.AnimationCacheFactory;
import software.bluelib.loader.json.animation.AnimationFormatVersion;
import software.bluelib.loader.json.controller.ControllerCacheFactory;
import software.bluelib.loader.json.controller.ControllerFormatVersion;
import software.bluelib.loader.json.deserialize.animation.AnimationLibrary;
import software.bluelib.loader.json.deserialize.controller.Behaviour;
import software.bluelib.loader.json.deserialize.controller.Controller;
import software.bluelib.loader.json.deserialize.controller.Group;
import software.bluelib.loader.json.deserialize.controller.State;
import software.bluelib.loader.json.deserialize.model.*;
import software.bluelib.loader.json.deserialize.variants.Entity;
import software.bluelib.loader.json.deserialize.variants.Variant;
import software.bluelib.loader.json.model.ModelCacheFactory;
import software.bluelib.loader.json.model.ModelFormatVersion;
import software.bluelib.loader.json.variants.VariantsCacheFactory;
import software.bluelib.loader.json.variants.VariantsFormatVersion;
import software.bluelib.oldLoader.loading.json.typeadapter.BakedAnimationsAdapter;
import software.bluelib.oldLoader.loading.json.typeadapter.KeyFramesAdapter;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class BlueLoader {

	@NotNull
	public static final Gson MODEL_GSON = new GsonBuilder().setLenient()
			.registerTypeAdapter(Bone.class, Bone.deserializer())
			.registerTypeAdapter(Cube.class, Cube.deserializer())
			.registerTypeAdapter(FaceUV.class, FaceUV.deserializer())
			.registerTypeAdapter(LocatorClass.class, LocatorClass.deserializer())
			.registerTypeAdapter(LocatorValue.class, LocatorValue.deserializer())
			.registerTypeAdapter(ModelGeometry.class, ModelGeometry.deserializer())
			.registerTypeAdapter(Model.class, Model.deserializer())
			.registerTypeAdapter(ModelDescription.class, ModelDescription.deserializer())
			.registerTypeAdapter(PolyMesh.class, PolyMesh.deserializer())
			.registerTypeAdapter(PolysUnion.class, PolysUnion.deserializer())
			.registerTypeAdapter(TextureMesh.class, TextureMesh.deserializer())
			.registerTypeAdapter(UVFaces.class, UVFaces.deserializer())
			.registerTypeAdapter(UVUnion.class, UVUnion.deserializer())
			.create();

	@NotNull
	public static final Gson ANIMATION_GSON = new GsonBuilder().setLenient()
			.registerTypeAdapter(KeyframeLibraryCache.class, new KeyFramesAdapter())
			.registerTypeAdapter(AnimationLibraryCache.class, new BakedAnimationsAdapter())
			.create();

	@NotNull
	public static final Gson CONTROLLER_GSON = new GsonBuilder().setLenient()
			.registerTypeAdapter(Controller.class, Controller.deserializer())
			.registerTypeAdapter(Group.class, Group.deserializer())
			.registerTypeAdapter(Behaviour.class, Behaviour.deserializer())
			.registerTypeAdapter(State.class, State.deserializer())
			.create();

	@NotNull
	public static final Gson VARIANTS_GSON = new GsonBuilder()
			.registerTypeAdapter(Entity.class, Entity.deserializer())
			.registerTypeAdapter(Variant.class, Variant.deserializer())
			.setPrettyPrinting()
			.create();

	@NotNull
	private static ResourceLocation stripPrefixAndSuffix(@NotNull ResourceLocation pResourceLocation) {
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("strip.prefix_suffix", pResourceLocation.toString()));
		String newPath = pResourceLocation.getPath();
		Matcher prefixMatcher = BlueLibConstants.BlueLoader.PREFIX_STRIPPER.matcher(newPath);
		newPath = prefixMatcher.find() ? newPath.substring(prefixMatcher.end()) : newPath;
		Matcher suffixMatcher = BlueLibConstants.BlueLoader.SUFFIX_STRIPPER.matcher(newPath);
		newPath = suffixMatcher.find() ? newPath.substring(0, suffixMatcher.start()) : newPath;

		ResourceLocation result = newPath.length() == pResourceLocation.getPath().length() ? pResourceLocation : pResourceLocation.withPath(newPath);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("strip.result", result.toString()));
		return result;
	}

	protected static CompletableFuture<Map<ResourceLocation, ControllerCache>> loadControllers(Executor pBackgroundExecutor, ResourceManager pResourceManager) {
		return bakeJsonResources(pBackgroundExecutor, pResourceManager, BlueLibConstants.BlueLoader.CONTROLLERS_PATH.getPath(), ResourceCache::bakeController,
				ex -> null);
	}

	protected static CompletableFuture<Map<ResourceLocation, AnimationLibraryCache>> loadAnimations(Executor pBackgroundExecutor, ResourceManager pResourceManager) {
		return bakeJsonResources(pBackgroundExecutor, pResourceManager, BlueLibConstants.BlueLoader.ANIMATIONS_PATH.getPath(), ResourceCache::bakeAnimations,
				ex -> new AnimationLibraryCache(new Object2ObjectOpenHashMap<>()));
	}

	protected static CompletableFuture<Map<ResourceLocation, ModelCache>> loadModels(Executor pBackgroundExecutor, ResourceManager pResourceManager) {
		return bakeJsonResources(pBackgroundExecutor, pResourceManager, BlueLibConstants.BlueLoader.MODELS_PATH.getPath(), ResourceCache::bakeModel,
				ex -> null);
	}

	@NotNull
	protected static CompletableFuture<Map<ResourceLocation, EntityCache>> loadVariants(
			@NotNull Executor pBackgroundExecutor,
			@NotNull ResourceManager pResourceManager,
			@NotNull List<IVariantProvider> pProviders) {
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.load.start", pProviders.size()));
		List<CompletableFuture<Map.Entry<ResourceLocation, EntityCache>>> futures = new ObjectArrayList<>();

		for (IVariantProvider provider : pProviders) {
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.provider", provider.getBasePath()));
			for (String entity : provider.getEntityNames()) {
				if (BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPre(entity)) {
					BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.load.cancelled", entity));
					continue;
				}

				String entityPath = provider.getBasePath() + entity;
				BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.entity.processing", entityPath));

				Map<ResourceLocation, Resource> resources = pResourceManager.listResources(entityPath, fileName -> fileName.getPath().endsWith(".json"));
				JsonObject merged = new JsonObject();
				for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
					JsonObject obj = readJsonFile(entry.getKey(), entry.getValue());
					new JSONMerger().mergeJsonObjects(merged, obj);
				}

				String namespace = resources.isEmpty() ? "minecraft" : resources.keySet().iterator().next().getNamespace();
				ResourceLocation key = ResourceLocation.fromNamespaceAndPath(namespace, entity);

				futures.add(CompletableFuture.supplyAsync(() -> {
					if (BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPre(entity, key.toString())) {
						BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.variant.load.cancelled", key));
						return null;
					}
					EntityCache cache = bakeVariants(key, merged);
					BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPost(entity, key.toString());
					return Map.entry(key, cache);
				}, pBackgroundExecutor));
				BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPost(entity);
			}
		}

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(ignored -> {
					Map<ResourceLocation, EntityCache> combined = new java.util.HashMap<>();
					for (CompletableFuture<Map.Entry<ResourceLocation, EntityCache>> future : futures) {
						Map.Entry<ResourceLocation, EntityCache> entry = future.join();
						if (entry != null) {
							combined.put(entry.getKey(), entry.getValue());
						}
					}
					return combined;
				});
	}

	@NotNull
	protected static <BAKED> CompletableFuture<Map<ResourceLocation, BAKED>> bakeJsonResources(
			@NotNull Executor pBackgroundExecutor,
			@NotNull ResourceManager pResourceManager,
			@NotNull String pAssetPath,
			@NotNull BiFunction<ResourceLocation, JsonObject, BAKED> pElementFactory,
			@NotNull Function<Throwable, BAKED> pExceptionalFactory) {
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("bakejson.start", pAssetPath));
		return loadResources(pBackgroundExecutor, pResourceManager, pAssetPath, "json", ResourceCache::readJsonFile)
				.thenCompose(resources -> {
					BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("bakejson.resources.loaded", resources.size()));
					List<CompletableFuture<Pair<ResourceLocation, BAKED>>> tasks = new ObjectArrayList<>(resources.size());
					BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("bakejson.resources.tobake", Arrays.toString(resources.stream().map(Pair::left).toList().toArray())));

					resources.forEach(pair -> tasks.add(
							CompletableFuture.supplyAsync(() -> {
										BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("bakejson.baking", pair.left().toString()));
										try {
											Pair<ResourceLocation, BAKED> baked = Pair.of(stripPrefixAndSuffix(pair.left()), pElementFactory.apply(pair.left(), pair.right()));
											BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("bakejson.baked", pair.left().toString()));
											return baked;
										} catch (Exception ex) {
											BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("bakejson.error", pair.left().toString(), ex.getMessage()));
											throw ex;
										}
									}, pBackgroundExecutor)
									.exceptionally(ex -> {
										BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("bakejson.exceptionally", pair.left().toString(), ex.getMessage()));
										ex.printStackTrace();
										return Pair.of(pair.left(), pExceptionalFactory.apply(ex));
									})));

					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
							.thenApply(ignored -> {
								BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("bakejson.tasks.completed"));
								return tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toMap(Pair::left, Pair::right));
							});
				});
	}

	@NotNull
	protected static <UNBAKED> CompletableFuture<List<Pair<ResourceLocation, UNBAKED>>> loadResources(
			@NotNull Executor pExecutor,
			@NotNull ResourceManager pResourceManager,
			@NotNull String pAssetPath,
			@NotNull String pFileType,
			@NotNull BiFunction<ResourceLocation, Resource, UNBAKED> pElementFactory) {
		final String fileTypeSuffix = "." + pFileType;
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.listing", pAssetPath, pFileType));

		return CompletableFuture.supplyAsync(() -> {
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.listresources", pAssetPath, fileTypeSuffix));
			Map<ResourceLocation, Resource> allResources = pResourceManager.listResources(pAssetPath, fileName -> fileName.getPath().endsWith(fileTypeSuffix));
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.found", Arrays.toString(allResources.keySet().toArray())));

			Map<ResourceLocation, Resource> listed = allResources.entrySet().stream()
					.filter(entry -> !BlueLibConstants.BlueLoader.SKIPPED_NAMESPACES.contains(entry.getKey().getNamespace()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.filtered", Arrays.toString(listed.keySet().toArray())));
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.count", listed.size()));
			return listed;
		}, pExecutor).thenCompose(filteredResources -> {
			List<CompletableFuture<Pair<ResourceLocation, UNBAKED>>> tasks = new ObjectArrayList<>(filteredResources.size());

			filteredResources.forEach((path, resource) -> {
				BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.loading", path.toString(), resource.toString()));
				tasks.add(CompletableFuture.supplyAsync(() -> {
					BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.elementfactory", path.toString()));
					return Pair.of(path, pElementFactory.apply(path, resource));
				}, pExecutor));
			});
			return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
					.thenApply(ignored -> {
						BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("resources.tasks.completed"));
						return tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList();
					});
		});
	}

	@NotNull
	protected static ModelCache bakeModel(@NotNull ResourceLocation pResourceLocation, @NotNull JsonObject pJsonObject) {
		return bakeGeneric(
				pResourceLocation,
				pJsonObject,
				MODEL_GSON,
				Model.class,
				Model::formatVersion,
				ModelFormatVersion.REGISTRY::match,
				ModelFormatVersion::isSupported,
				ModelFormatVersion::getErrorMessage,
				(namespace, model) -> CacheFactory.constructWithFactory(ModelCacheFactory.REGISTRY::getForNamespace, namespace, model),
				List.of(
						Pair.of(loc -> loc.getPath().endsWith(".animation.json"), ".animation.json"),
						Pair.of(loc -> loc.getPath().endsWith(".controller.json"), ".controller.json")));
	}

	@NotNull
	protected static AnimationLibraryCache bakeAnimations(@NotNull ResourceLocation pResourceLocation, @NotNull JsonObject pJsonObject) {
		return bakeGeneric(
				pResourceLocation,
				pJsonObject,
				ANIMATION_GSON,
				AnimationLibrary.class,
				AnimationLibrary::formatVersion,
				AnimationFormatVersion.REGISTRY::match,
				AnimationFormatVersion::isSupported,
				AnimationFormatVersion::getErrorMessage,
				(namespace, animations) -> CacheFactory.constructWithFactory(AnimationCacheFactory.REGISTRY::getForNamespace, namespace, animations),
				List.of(
						Pair.of(loc -> loc.getPath().endsWith(".geo.json"), ".geo.json"),
						Pair.of(loc -> loc.getPath().endsWith(".controller.json"), ".controller.json")));
	}

	@NotNull
	protected static ControllerCache bakeController(@NotNull ResourceLocation pResourceLocation, @NotNull JsonObject pJsonObject) {
		return bakeGeneric(
				pResourceLocation,
				pJsonObject,
				CONTROLLER_GSON,
				Controller.class,
				Controller::formatVersion,
				ControllerFormatVersion.REGISTRY::match,
				ControllerFormatVersion::isSupported,
				ControllerFormatVersion::getErrorMessage,
				(namespace, controller) -> CacheFactory.constructWithFactory(ControllerCacheFactory.REGISTRY::getForNamespace, namespace, controller),
				List.of(
						Pair.of(loc -> loc.getPath().endsWith(".geo.json"), ".geo.json"),
						Pair.of(loc -> loc.getPath().endsWith(".animation.json"), ".animation.json")));
	}

	@NotNull
	protected static EntityCache bakeVariants(@NotNull ResourceLocation pResourceLocation, @NotNull JsonObject pJsonObject) {
		return bakeGeneric(
				pResourceLocation,
				pJsonObject,
				VARIANTS_GSON,
				Entity.class,
				Entity::formatVersion,
				VariantsFormatVersion.REGISTRY::match,
				VariantsFormatVersion::isSupported,
				VariantsFormatVersion::getErrorMessage,
				(namespace, controller) -> CacheFactory.constructWithFactory(VariantsCacheFactory.REGISTRY::getForNamespace, namespace, controller),
				null);
	}

	@NotNull
	public static <T, V, C> C bakeGeneric(
			@NotNull ResourceLocation pResourceLocation,
			@NotNull JsonObject pJsonObject,
			@NotNull Gson pGson,
			@NotNull Class<T> pModelClass,
			@NotNull Function<T, String> pVersionExtractor,
			@NotNull Function<String, V> pVersionMatcher,
			@NotNull Predicate<V> pIsSupported,
			@NotNull Function<V, String> pErrorMessage,
			@NotNull BiFunction<String, T, C> pCacheFactory,
			@Nullable List<Pair<Predicate<ResourceLocation>, String>> pFileChecks) {
		if (pFileChecks != null) {
			String path = pResourceLocation.getPath();
			String folderName = path.contains("/") ? path.substring(0, path.indexOf('/')) : path;
			for (Pair<Predicate<ResourceLocation>, String> check : pFileChecks) {
				if (check.left().test(pResourceLocation)) {
					BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("bakegeneric.filecheck.failed", pResourceLocation.toString()));
					throw new RuntimeException(String.format("Found %s in %s folder! '%s'",
							check.right(), folderName, pResourceLocation));
				}
			}
		}
		T model = pGson.fromJson(pJsonObject, pModelClass);
		String version = pVersionExtractor.apply(model);
		V matchedVersion = pVersionMatcher.apply(version);

		if (matchedVersion == null) {
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("bakegeneric.version.unknown", pResourceLocation.toString(), version));
		} else if (!pIsSupported.test(matchedVersion)) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("bakegeneric.version.unsupported", pResourceLocation.toString(), version, pErrorMessage.apply(matchedVersion)));
		}

		return pCacheFactory.apply(pResourceLocation.getNamespace(), model);
	}

	@NotNull
	protected static JsonObject readJsonFile(@NotNull ResourceLocation pResourceLocation, @NotNull Resource pResource) {
		try (Reader reader = pResource.openAsReader()) {
			return GsonHelper.parse(reader);
		} catch (IOException pIoException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("readjson.failed", pResourceLocation.toString(), pIoException.getMessage()));
			throw new RuntimeException("Failed to read resource: " + pResourceLocation, pIoException);
		}
	}
}
