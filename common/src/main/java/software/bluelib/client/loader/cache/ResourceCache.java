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
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import software.bluelib.client.loader.BlueLoader;
import software.bluelib.client.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.client.loader.cache.controller.ControllerCache;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.loading.json.typeadapter.BakedAnimationsAdapter;

public final class ResourceCache extends BlueLoader {

	private static Map<ResourceLocation, ControllerCache> CONTROLLERS = Collections.emptyMap();
	private static Map<ResourceLocation, AnimationLibraryCache> ANIMATIONS = Collections.emptyMap();
	private static Map<ResourceLocation, ModelCache> MODELS = Collections.emptyMap();

	public static Map<ResourceLocation, AnimationLibraryCache> getBakedAnimations() {
		return ANIMATIONS;
	}

	public static Map<ResourceLocation, ModelCache> getBakedModels() {
		return MODELS;
	}

	public static void registerClientReloadListener() {
		Minecraft mc = Minecraft.getInstance();

		if (mc.getResourceManager() instanceof ReloadableResourceManager pResourceManager)
			pResourceManager.registerReloadListener(ResourceCache::reloadClient);
	}

	public static void registerServerReloadListener(MinecraftServer pServer) {
		ResourceCache.reloadServer(pServer.getResourceManager(), Util.backgroundExecutor(), pServer);
	}

	public static CompletableFuture<Void> reloadClient(
			PreparationBarrier pStage,
			ResourceManager pResourceManager,
			ProfilerFiller pProfilerFiller,
			ProfilerFiller pProfilerFiller1,
			Executor pBackgroundExecutor,
			Executor pGameExecutor) {
		clearClientCaches();

		CompletableFuture<Map<ResourceLocation, AnimationLibraryCache>> animations = loadAnimations(pBackgroundExecutor, pResourceManager);
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

	public static CompletableFuture<Void> reloadServer(
			ResourceManager pResourceManager,
			Executor pBackgroundExecutor,
			Executor pGameExecutor) {
		clearServerCaches();

		CompletableFuture<Map<ResourceLocation, ControllerCache>> controllers = loadControllers(pBackgroundExecutor, pResourceManager);

		return CompletableFuture.allOf(controllers)
				.thenRunAsync(() -> {
					ResourceCache.CONTROLLERS = controllers.join();

					System.out.println("Controller Cache: " + ResourceCache.CONTROLLERS);
				}, pGameExecutor);
	}

	private static void clearClientCaches() {
		ResourceCache.ANIMATIONS = Collections.emptyMap();
		ResourceCache.MODELS = Collections.emptyMap();
	}

	private static void clearServerCaches() {
		ResourceCache.CONTROLLERS = Collections.emptyMap();
	}
}
