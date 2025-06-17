/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import software.bluelib.client.loader.BlueLoader;
import software.bluelib.client.loader.cache.animations.AnimationsCache;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.loader.loading.json.typeadapter.BakedAnimationsAdapter;

public final class ResourceCache extends BlueLoader {

    private static Map<ResourceLocation, AnimationsCache> ANIMATIONS = Collections.emptyMap();
    private static Map<ResourceLocation, ModelCache> MODELS = Collections.emptyMap();

    public static Map<ResourceLocation, AnimationsCache> getBakedAnimations() {
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

    public static CompletableFuture<Void> reload(
            PreparationBarrier pStage,
            ResourceManager pResourceManager,
            ProfilerFiller pProfilerFiller,
            ProfilerFiller pProfilerFiller1,
            Executor pBackgroundExecutor,
            Executor pGameExecutor) {
        clearCaches();

        CompletableFuture<Map<ResourceLocation, AnimationsCache>> animations = loadAnimations(pBackgroundExecutor, pResourceManager);
        CompletableFuture<Map<ResourceLocation, ModelCache>> models = loadModels(pBackgroundExecutor, pResourceManager);

        return CompletableFuture.runAsync(() -> BakedAnimationsAdapter.COMPRESSION_CACHE = new ConcurrentHashMap<>(), pBackgroundExecutor)
                .thenCompose(ignored -> CompletableFuture.allOf(animations, models)
                        .thenCompose(pStage::wait)
                        .thenRunAsync(() -> {
                            ResourceCache.ANIMATIONS = animations.join();
                            ResourceCache.MODELS = models.join();
                            BakedAnimationsAdapter.COMPRESSION_CACHE = null;

                            System.out.println("Model Cache: " + ResourceCache.MODELS);
                            System.out.println("Animations Cache: " + ResourceCache.ANIMATIONS);
                        }, pGameExecutor));
    }

    private static void clearCaches() {
        ResourceCache.ANIMATIONS = Collections.emptyMap();
        ResourceCache.MODELS = Collections.emptyMap();
    }
}
