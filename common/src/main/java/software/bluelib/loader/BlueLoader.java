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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.variants.EntityCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.variants.Entity;
import software.bluelib.loader.json.deserialize.variants.Variant;
import software.bluelib.loader.json.variants.VariantsCacheFactory;
import software.bluelib.loader.json.variants.VariantsFormatVersion;

import java.io.IOException;
import java.io.Reader;
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
	public static final Gson VARIANTS_GSON = new GsonBuilder()
			.registerTypeAdapter(Entity.class, Entity.deserializer())
			.registerTypeAdapter(Variant.class, Variant.deserializer())
			.setPrettyPrinting()
			.create();

	private static ResourceLocation stripPrefixAndSuffix(ResourceLocation pResourceLocation) {
		String newPath = pResourceLocation.getPath();
		Matcher prefixMatcher = BlueLibConstants.BlueLoader.PREFIX_STRIPPER.matcher(newPath);
		newPath = prefixMatcher.find() ? newPath.substring(prefixMatcher.end()) : newPath;
		Matcher suffixMatcher = BlueLibConstants.BlueLoader.SUFFIX_STRIPPER.matcher(newPath);
		newPath = suffixMatcher.find() ? newPath.substring(0, suffixMatcher.start()) : newPath;

		return newPath.length() == pResourceLocation.getPath().length() ? pResourceLocation : pResourceLocation.withPath(newPath);
	}

	protected static CompletableFuture<Map<ResourceLocation, EntityCache>> loadVariants(
			Executor pBackgroundExecutor,
			ResourceManager pResourceManager,
			List<IVariantProvider> pProviders) {

		List<CompletableFuture<Map<ResourceLocation, EntityCache>>> futures = new ObjectArrayList<>();

		for (IVariantProvider provider : pProviders) {
			futures.add(
					bakeJsonResources(
							pBackgroundExecutor,
							pResourceManager,
							provider.getBasePath(),
							ResourceCache::bakeController,
							ex -> null
					)
			);
		}

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(ignored -> {
					Map<ResourceLocation, EntityCache> combined = new java.util.HashMap<>();
					for (CompletableFuture<Map<ResourceLocation, EntityCache>> future : futures) {
						Map<ResourceLocation, EntityCache> result = future.join();
						if (result != null) {
							combined.putAll(result);
						}
					}
					return combined;
				});
	}

	protected static <BAKED> CompletableFuture<Map<ResourceLocation, BAKED>> bakeJsonResources(Executor pBackgroundExecutor, ResourceManager pResourceManager, String pAssetPath,
	                                                                                           BiFunction<ResourceLocation, JsonObject, BAKED> pElementFactory, Function<Throwable, BAKED> pExceptionalFactory) {
		return loadResources(pBackgroundExecutor, pResourceManager, pAssetPath, "json", ResourceCache::readJsonFile)
				.thenCompose(resources -> {
					List<CompletableFuture<Pair<ResourceLocation, BAKED>>> tasks = new ObjectArrayList<>(resources.size());

					resources.forEach(pair -> tasks.add(
							CompletableFuture.supplyAsync(() -> {
										try {
											return Pair.of(stripPrefixAndSuffix(pair.left()), pElementFactory.apply(pair.left(), pair.right()));
										} catch (Exception ex) {
											System.err.println("Error deserializing file: " + pair.left());
											ex.printStackTrace();
											throw ex;
										}
									}, pBackgroundExecutor)
									.exceptionally(ex -> {
										ex.printStackTrace();
										return Pair.of(pair.left(), pExceptionalFactory.apply(ex));
									})));

					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
							.thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toMap(Pair::left, Pair::right)));
				});
	}

	protected static <UNBAKED> CompletableFuture<List<Pair<ResourceLocation, UNBAKED>>> loadResources(
			Executor pExecutor,
			ResourceManager pResourceManager,
			String pAssetPath,
			String pFileType,
			BiFunction<ResourceLocation, Resource, UNBAKED> pElementFactory) {
		final String fileTypeSuffix = "." + pFileType;

		return CompletableFuture.supplyAsync(() -> pResourceManager.listResources(pAssetPath, fileName -> fileName.getPath().endsWith(fileTypeSuffix))
						.entrySet().stream()
						.filter(entry -> !BlueLibConstants.BlueLoader.SKIPPED_NAMESPACES.contains(entry.getKey().getNamespace()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
				pExecutor).thenCompose(filteredResources -> {
			List<CompletableFuture<Pair<ResourceLocation, UNBAKED>>> tasks = new ObjectArrayList<>(filteredResources.size());

			filteredResources.forEach((path, resource) -> {
				System.out.println("Loading path: " + path + " with resource: " + resource);
				tasks.add(CompletableFuture.supplyAsync(() -> Pair.of(path, pElementFactory.apply(path, resource)), pExecutor));
			});
			return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
					.thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList());
		});
	}

	@NotNull
	protected static EntityCache bakeController(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
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
				List.of(
						Pair.of(loc -> loc.getPath().endsWith(".geo.json"), ".geo.json"),
						Pair.of(loc -> loc.getPath().endsWith(".animation.json"), ".animation.json")));
	}

	public static <T, V, C> C bakeGeneric(
			ResourceLocation pResourceLocation,
			JsonObject pJsonObject,
			Gson pGson,
			Class<T> pModelClass,
			Function<T, String> pVersionExtractor,
			Function<String, V> pVersionMatcher,
			Predicate<V> pIsSupported,
			Function<V, String> pErrorMessage,
			BiFunction<String, T, C> pCacheFactory,
			List<Pair<Predicate<ResourceLocation>, String>> pFileChecks) {
		if (pFileChecks != null) {
			String path = pResourceLocation.getPath();
			String folderName = path.contains("/") ? path.substring(0, path.indexOf('/')) : path;
			for (Pair<Predicate<ResourceLocation>, String> check : pFileChecks) {
				if (check.left().test(pResourceLocation)) {
					throw new RuntimeException(String.format("Found %s in %s folder! '%s'",
							check.right(), folderName, pResourceLocation));
				}
			}
		}
		T model = pGson.fromJson(pJsonObject, pModelClass);
		String version = pVersionExtractor.apply(model);
		V matchedVersion = pVersionMatcher.apply(version);

		if (matchedVersion == null) {
			System.out.printf("%s: Unknown format version: '%s'. This may not work correctly%n", pResourceLocation, version);
		} else if (!pIsSupported.test(matchedVersion)) {
			System.out.printf("%s: Unsupported format version: '%s'. %s%n", pResourceLocation, version, pErrorMessage.apply(matchedVersion));
		}

		return pCacheFactory.apply(pResourceLocation.getNamespace(), model);
	}

	protected static JsonObject readJsonFile(ResourceLocation pResourceLocation, Resource pResource) {
		try (Reader reader = pResource.openAsReader()) {
			return GsonHelper.parse(reader);
		} catch (IOException pIoException) {
			throw new RuntimeException("Failed to read resource: " + pResourceLocation, pIoException);
		}
	}
}
