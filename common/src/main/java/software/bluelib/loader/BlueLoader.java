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
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.cache.ResourceCache;
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
		BaseLogger.log(true, BaseLogLevel.INFO, "Stripping prefix and suffix for: " + pResourceLocation);
		String newPath = pResourceLocation.getPath();
		Matcher prefixMatcher = BlueLibConstants.BlueLoader.PREFIX_STRIPPER.matcher(newPath);
		newPath = prefixMatcher.find() ? newPath.substring(prefixMatcher.end()) : newPath;
		Matcher suffixMatcher = BlueLibConstants.BlueLoader.SUFFIX_STRIPPER.matcher(newPath);
		newPath = suffixMatcher.find() ? newPath.substring(0, suffixMatcher.start()) : newPath;

		ResourceLocation result = newPath.length() == pResourceLocation.getPath().length() ? pResourceLocation : pResourceLocation.withPath(newPath);
		BaseLogger.log(true, BaseLogLevel.INFO, "Result after strip: " + result);
		return result;
	}

	protected static CompletableFuture<Map<ResourceLocation, Entity>> loadVariants(
			Executor pBackgroundExecutor,
			ResourceManager pResourceManager,
			List<IVariantProvider> pProviders) {

		BaseLogger.log(true, BaseLogLevel.INFO, "Starting loadVariants with providers: " + pProviders.size());
		List<CompletableFuture<Map<ResourceLocation, Entity>>> futures = new ObjectArrayList<>();

		for (IVariantProvider provider : pProviders) {
			BaseLogger.log(true, BaseLogLevel.INFO, "Processing provider: " + provider.getBasePath());
			for (String entity : provider.getEntityNames()) {
				String entityPath = provider.getBasePath() + entity + "/";
				BaseLogger.log(true, BaseLogLevel.INFO, "Processing entity: " + entityPath);
				futures.add(
						bakeJsonResources(
								pBackgroundExecutor,
								pResourceManager,
								entityPath,
								ResourceCache::bakeVariants,
								ex -> null
						)
				);
			}
		}

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(ignored -> {
					BaseLogger.log(true, BaseLogLevel.INFO, "All futures completed for loadVariants");
					Map<ResourceLocation, Entity> combined = new java.util.HashMap<>();
					for (CompletableFuture<Map<ResourceLocation, Entity>> future : futures) {
						Map<ResourceLocation, Entity> result = future.join();
						if (result != null) {
							BaseLogger.log(true, BaseLogLevel.INFO, "Merging result with size: " + result.size());
							combined.putAll(result);
						}
					}
					BaseLogger.log(true, BaseLogLevel.INFO, "Combined map size: " + combined.size());
					return combined;
				});
	}

	protected static <BAKED> CompletableFuture<Map<ResourceLocation, BAKED>> bakeJsonResources(Executor pBackgroundExecutor, ResourceManager pResourceManager, String pAssetPath,
	                                                                                           BiFunction<ResourceLocation, JsonObject, BAKED> pElementFactory, Function<Throwable, BAKED> pExceptionalFactory) {
		BaseLogger.log(true, BaseLogLevel.INFO, "Starting bakeJsonResources for assetPath: " + pAssetPath);
		return loadResources(pBackgroundExecutor, pResourceManager, pAssetPath, "json", ResourceCache::readJsonFile)
				.thenCompose(resources -> {
					BaseLogger.log(true, BaseLogLevel.INFO, "Loaded resources: " + resources.size());
					List<CompletableFuture<Pair<ResourceLocation, BAKED>>> tasks = new ObjectArrayList<>(resources.size());

					resources.forEach(pair -> tasks.add(
							CompletableFuture.supplyAsync(() -> {
										BaseLogger.log(true, BaseLogLevel.INFO, "Baking resource: " + pair.left());
										try {
											Pair<ResourceLocation, BAKED> baked = Pair.of(stripPrefixAndSuffix(pair.left()), pElementFactory.apply(pair.left(), pair.right()));
											BaseLogger.log(true, BaseLogLevel.INFO, "Baked resource: " + pair.left());
											return baked;
										} catch (Exception ex) {
											BaseLogger.log(true, BaseLogLevel.ERROR, "Error deserializing file: " + pair.left() + " - " + ex.getMessage());
											throw ex;
										}
									}, pBackgroundExecutor)
									.exceptionally(ex -> {
										BaseLogger.log(true, BaseLogLevel.ERROR, "Exceptionally handling: " + pair.left() + " - " + ex.getMessage());
										ex.printStackTrace();
										return Pair.of(pair.left(), pExceptionalFactory.apply(ex));
									})));

					return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
							.thenApply(ignored -> {
								BaseLogger.log(true, BaseLogLevel.INFO, "All baking tasks completed");
								return tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toMap(Pair::left, Pair::right));
							});
				});
	}

	protected static <UNBAKED> CompletableFuture<List<Pair<ResourceLocation, UNBAKED>>> loadResources(
			Executor pExecutor,
			ResourceManager pResourceManager,
			String pAssetPath,
			String pFileType,
			BiFunction<ResourceLocation, Resource, UNBAKED> pElementFactory) {
		final String fileTypeSuffix = "." + pFileType;
		BaseLogger.log(true, BaseLogLevel.INFO, "Listing resources for path: " + pAssetPath + " with type: " + pFileType);

		return CompletableFuture.supplyAsync(() -> {
			Map<ResourceLocation, Resource> listed = pResourceManager.listResources(pAssetPath, fileName -> fileName.getPath().endsWith(fileTypeSuffix))
					.entrySet().stream()
					.filter(entry -> !BlueLibConstants.BlueLoader.SKIPPED_NAMESPACES.contains(entry.getKey().getNamespace()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			BaseLogger.log(true, BaseLogLevel.INFO, "Resource keys found after filtering: " + listed.keySet());
			BaseLogger.log(true, BaseLogLevel.INFO, "Listed resources: " + listed.size());
			return listed;
		}, pExecutor).thenCompose(filteredResources -> {
			List<CompletableFuture<Pair<ResourceLocation, UNBAKED>>> tasks = new ObjectArrayList<>(filteredResources.size());

			filteredResources.forEach((path, resource) -> {
				BaseLogger.log(true, BaseLogLevel.INFO, "Loading path: " + path + " with resource: " + resource);
				tasks.add(CompletableFuture.supplyAsync(() -> {
					BaseLogger.log(true, BaseLogLevel.INFO, "Applying element factory for: " + path);
					return Pair.of(path, pElementFactory.apply(path, resource));
				}, pExecutor));
			});
			return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
					.thenApply(ignored -> {
						BaseLogger.log(true, BaseLogLevel.INFO, "All resource loading tasks completed");
						return tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList();
					});
		});
	}

	@NotNull
	protected static Entity bakeVariants(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
		BaseLogger.log(true, BaseLogLevel.INFO, "Baking variants for: " + pResourceLocation);
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
			@Nullable List<Pair<Predicate<ResourceLocation>, String>> pFileChecks) {
		BaseLogger.log(true, BaseLogLevel.INFO, "Starting bakeGeneric for: " + pResourceLocation);
		if (pFileChecks != null) {
			String path = pResourceLocation.getPath();
			String folderName = path.contains("/") ? path.substring(0, path.indexOf('/')) : path;
			for (Pair<Predicate<ResourceLocation>, String> check : pFileChecks) {
				if (check.left().test(pResourceLocation)) {
					BaseLogger.log(true, BaseLogLevel.ERROR, "File check failed for: " + pResourceLocation);
					throw new RuntimeException(String.format("Found %s in %s folder! '%s'",
							check.right(), folderName, pResourceLocation));
				}
			}
		}
		T model = pGson.fromJson(pJsonObject, pModelClass);
		BaseLogger.log(true, BaseLogLevel.INFO, "Deserialized model for: " + pResourceLocation);
		String version = pVersionExtractor.apply(model);
		BaseLogger.log(true, BaseLogLevel.INFO, "Extracted version: " + version + " for: " + pResourceLocation);
		V matchedVersion = pVersionMatcher.apply(version);

		if (matchedVersion == null) {
			BaseLogger.log(true, BaseLogLevel.WARNING, pResourceLocation + ": Unknown format version: '" + version + "'. This may not work correctly");
		} else if (!pIsSupported.test(matchedVersion)) {
			BaseLogger.log(true, BaseLogLevel.ERROR, pResourceLocation + ": Unsupported format version: '" + version + "'. " + pErrorMessage.apply(matchedVersion));
		}

		C cache = pCacheFactory.apply(pResourceLocation.getNamespace(), model);
		BaseLogger.log(true, BaseLogLevel.INFO, "Cache created for: " + pResourceLocation);
		return cache;
	}

	protected static JsonObject readJsonFile(ResourceLocation pResourceLocation, Resource pResource) {
		BaseLogger.log(true, BaseLogLevel.INFO, "Reading JSON file for: " + pResourceLocation);
		try (Reader reader = pResource.openAsReader()) {
			JsonObject obj = GsonHelper.parse(reader);
			BaseLogger.log(true, BaseLogLevel.INFO, "Parsed JSON for: " + pResourceLocation);
			return obj;
		} catch (IOException pIoException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, "Failed to read resource: " + pResourceLocation + " - " + pIoException.getMessage());
			throw new RuntimeException("Failed to read resource: " + pResourceLocation, pIoException);
		}
	}
}