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
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.exception.CompoundException;
import software.bluelib.client.loader.cache.ResourceCache;
import software.bluelib.client.loader.cache.animations.AnimationsCache;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.loader.json.model.ModelCacheFactory;
import software.bluelib.client.loader.json.model.ModelFormatVersion;
import software.bluelib.client.loader.json.model.deserialize.*;
import software.bluelib.client.loader.json.model.object.BoneTree;
import software.bluelib.loader.animation.Animation;
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
            .registerTypeAdapter(Animation.Keyframes.class, new KeyFramesAdapter())
            .registerTypeAdapter(AnimationsCache.class, new BakedAnimationsAdapter())
            .create();

    public static ResourceLocation stripPrefixAndSuffix(ResourceLocation pResourceLocation) {
        String newPath = pResourceLocation.getPath();
        Matcher prefixMatcher = BlueLibConstants.BlueLoader.PREFIX_STRIPPER.matcher(newPath);
        newPath = prefixMatcher.find() ? newPath.substring(prefixMatcher.end()) : newPath;
        Matcher suffixMatcher = BlueLibConstants.BlueLoader.SUFFIX_STRIPPER.matcher(newPath);
        newPath = suffixMatcher.find() ? newPath.substring(0, suffixMatcher.start()) : newPath;

        return newPath.length() == pResourceLocation.getPath().length() ? pResourceLocation : pResourceLocation.withPath(newPath);
    }

    protected static CompletableFuture<Map<ResourceLocation, AnimationsCache>> loadAnimations(Executor pBackgroundExecutor, ResourceManager pResourceManager) {
        return bakeJsonResources(pBackgroundExecutor, pResourceManager, BlueLibConstants.BlueLoader.ANIMATIONS_PATH.getPath(), ResourceCache::bakeAnimations,
                ex -> new AnimationsCache(new Object2ObjectOpenHashMap<>()));
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

                    filteredResources.forEach((path, resource) -> tasks.add(
                            CompletableFuture.supplyAsync(() -> Pair.of(path, pElementFactory.apply(path, resource)), pExecutor)));

                    return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                            .thenApply(ignored -> tasks.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList());
                });
    }

    @NotNull
    protected static ModelCache bakeModel(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
        if (pResourceLocation.getPath().endsWith(".animation.json"))
            throw new RuntimeException("Found animation file found in models folder! '" + pResourceLocation + "'");

        Model model = BlueLoader.MODEL_GSON.fromJson(pJsonObject, Model.class);
        ModelFormatVersion matchedVersion = ModelFormatVersion.match(model.modelFormatVersion());

        if (matchedVersion == null) {
            System.out.printf("%s: Unknown Blue model format version: '%s'. This may not work correctly%n", pResourceLocation, model.modelFormatVersion());
        } else if (!matchedVersion.isSupported()) {
            System.out.printf("%s: Unsupported Blue model format version: '%s'. %s%n", pResourceLocation, model.modelFormatVersion(), matchedVersion.getErrorMessage());
        }

        return ModelCacheFactory.getForNamespace(pResourceLocation.getNamespace()).constructBlueModel(BoneTree.fromModel(model));
    }

    @NotNull
    protected static AnimationsCache bakeAnimations(ResourceLocation pResourceLocation, JsonObject pJsonObject) {
        if (pResourceLocation.getPath().endsWith(".geo.json"))
            throw new RuntimeException("Found model file in animations folder! '" + pResourceLocation + "'");

        try {
            return BlueLoader.MODEL_GSON.fromJson(GsonHelper.getAsJsonObject(pJsonObject, "animations"), AnimationsCache.class);
        } catch (CompoundException ex) {
            throw ex.withMessage(pResourceLocation + ": Error building animations from JSON");
        } catch (Exception pException) {
            throw new RuntimeException(pResourceLocation + ": Error building animations from JSON", pException);
        }
    }

    protected static JsonObject readJsonFile(ResourceLocation pResourceLocation, Resource pResource) {
        try (Reader reader = pResource.openAsReader()) {
            return GsonHelper.parse(reader);
        } catch (IOException pIoException) {
            throw new RuntimeException("Failed to read resource: " + pResourceLocation, pIoException);
        }
    }
}
