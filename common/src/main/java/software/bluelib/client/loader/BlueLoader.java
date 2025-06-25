/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.loader.cache.ResourceCache;
import software.bluelib.client.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.client.loader.cache.animations.keyframe.KeyframeLibraryCache;
import software.bluelib.client.loader.cache.controller.ControllerCache;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.loader.json.CacheFactory;
import software.bluelib.client.loader.json.animation.AnimationCacheFactory;
import software.bluelib.client.loader.json.animation.AnimationFormatVersion;
import software.bluelib.client.loader.json.controller.ControllerCacheFactory;
import software.bluelib.client.loader.json.controller.ControllerFormatVersion;
import software.bluelib.client.loader.json.deserialize.animation.AnimationLibrary;
import software.bluelib.client.loader.json.deserialize.controller.Behaviour;
import software.bluelib.client.loader.json.deserialize.controller.Controller;
import software.bluelib.client.loader.json.deserialize.controller.Group;
import software.bluelib.client.loader.json.deserialize.controller.State;
import software.bluelib.client.loader.json.deserialize.model.*;
import software.bluelib.client.loader.json.model.ModelCacheFactory;
import software.bluelib.client.loader.json.model.ModelFormatVersion;
import software.bluelib.loader.loading.json.typeadapter.BakedAnimationsAdapter;
import software.bluelib.loader.loading.json.typeadapter.KeyFramesAdapter;

public class BlueLoader {

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

    public static final Gson ANIMATION_GSON = new GsonBuilder().setLenient()
            .registerTypeAdapter(KeyframeLibraryCache.class, new KeyFramesAdapter())
            .registerTypeAdapter(AnimationLibraryCache.class, new BakedAnimationsAdapter())
            .create();

    public static final Gson CONTROLLER_GSON = new GsonBuilder().setLenient()
            .registerTypeAdapter(Controller.class, Controller.deserializer())
            .registerTypeAdapter(Group.class, Group.deserializer())
            .registerTypeAdapter(Behaviour.class, Behaviour.deserializer())
            .registerTypeAdapter(State.class, State.deserializer())
            .create();

    private static ResourceLocation stripPrefixAndSuffix(ResourceLocation pResourceLocation) {
        String newPath = pResourceLocation.getPath();
        Matcher prefixMatcher = BlueLibConstants.BlueLoader.PREFIX_STRIPPER.matcher(newPath);
        newPath = prefixMatcher.find() ? newPath.substring(prefixMatcher.end()) : newPath;
        Matcher suffixMatcher = BlueLibConstants.BlueLoader.SUFFIX_STRIPPER.matcher(newPath);
        newPath = suffixMatcher.find() ? newPath.substring(0, suffixMatcher.start()) : newPath;

        return newPath.length() == pResourceLocation.getPath().length() ? pResourceLocation : pResourceLocation.withPath(newPath);
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
    protected static ModelCache bakeModel(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
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
    protected static AnimationLibraryCache bakeAnimations(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
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
    protected static ControllerCache bakeController(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
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
