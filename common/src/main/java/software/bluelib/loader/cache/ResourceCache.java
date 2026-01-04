/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.BlueLoader;
import software.bluelib.loader.cache.animation.AnimationFileCache;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.cache.variants.EntityCache;
import software.bluelib.loader.geckolib.animations.BakedAnimationsAdapter;
import software.bluelib.net.messages.client.loader.ControllerCachePacket;

public class ResourceCache extends BlueLoader {

	public static class Client {

		@NotNull
		private static Map<ResourceLocation, AnimationFileCache> ANIMATIONS = Collections.emptyMap();
		@NotNull
		private static Map<ResourceLocation, ModelCache> MODELS = Collections.emptyMap();

		@NotNull
		private static Map<ResourceLocation, ControllerCache> CONTROLLERS = Collections.emptyMap();

		@NotNull
		public static Map<ResourceLocation, AnimationFileCache> getBakedAnimations() {
			return ANIMATIONS;
		}

		@NotNull
		public static Map<ResourceLocation, ModelCache> getBakedModels() {
			return MODELS;
		}

		@NotNull
		public static Map<ResourceLocation, ControllerCache> getControllers() {
			return CONTROLLERS;
		}

		public static void setControllers(@NotNull Map<ResourceLocation, ControllerCache> pControllers) {
			CONTROLLERS = pControllers;
		}

		public static void registerReloadListener() {
			Minecraft mc = Minecraft.getInstance();

			if (mc.getResourceManager() instanceof ReloadableResourceManager pResourceManager)
				pResourceManager.registerReloadListener(ResourceCache.Client::reload);
		}

		@NotNull
		public static CompletableFuture<Void> reload(
				@NotNull PreparableReloadListener.PreparationBarrier pStage,
				@NotNull ResourceManager pResourceManager,
				@NotNull ProfilerFiller pProfilerFiller,
				@NotNull ProfilerFiller pProfilerFiller1,
				@NotNull Executor pBackgroundExecutor,
				@NotNull Executor pGameExecutor) {
			clearCaches();

			CompletableFuture<Map<ResourceLocation, AnimationFileCache>> animations = loadAnimations(pBackgroundExecutor, pResourceManager);
			CompletableFuture<Map<ResourceLocation, ModelCache>> models = loadModels(pBackgroundExecutor, pResourceManager);

			return CompletableFuture.runAsync(() -> BakedAnimationsAdapter.COMPRESSION_CACHE = new ConcurrentHashMap<>(), pBackgroundExecutor)
					.thenCompose(ignored -> CompletableFuture.allOf(animations, models)
							.thenCompose(pStage::wait)
							.thenRunAsync(() -> {
								ResourceCache.Client.ANIMATIONS = animations.join();
								ResourceCache.Client.MODELS = models.join();
								BakedAnimationsAdapter.COMPRESSION_CACHE = null;

								BaseLogger.log(true, BaseLogLevel.INFO, "Model Cache: " + ResourceCache.Client.MODELS);
								BaseLogger.log(true, BaseLogLevel.INFO, "Animations Cache: " + ResourceCache.Client.ANIMATIONS);
							}, pGameExecutor));
		}

		private static void clearCaches() {
			ResourceCache.Client.ANIMATIONS = Collections.emptyMap();
			ResourceCache.Client.MODELS = Collections.emptyMap();
		}
	}

	public static class Server {

		@NotNull
		public static Map<ResourceLocation, EntityCache> VARIANTS = Collections.emptyMap();
		@NotNull
		private static Map<ResourceLocation, ControllerCache> CONTROLLERS = Collections.emptyMap();

		@NotNull
		public static Map<ResourceLocation, EntityCache> getVariants() {
			return VARIANTS;
		}

		@NotNull
		public static Map<ResourceLocation, ControllerCache> getControllers() {
			return CONTROLLERS;
		}

		public static void registerReloadListener(@NotNull MinecraftServer pServer, @NotNull List<IVariantProvider> pProviders) {
			ResourceCache.Server.reload(pProviders, pServer, Util.backgroundExecutor(), pServer);
		}

		public static CompletableFuture<Void> reload(
				@NotNull List<IVariantProvider> pProviders,
				@NotNull MinecraftServer pServer,
				@NotNull Executor pBackgroundExecutor,
				@NotNull Executor pGameExecutor) {
			clearCaches();

			CompletableFuture<Map<ResourceLocation, ControllerCache>> controllers = loadControllers(pBackgroundExecutor, pServer.getResourceManager())
					.exceptionally(ex -> {
						BaseLogger.log(true, BaseLogLevel.ERROR, "controllers failed: " + ex.getMessage());
						return java.util.Collections.emptyMap();
					});

			CompletableFuture<Map<ResourceLocation, EntityCache>> variants = loadVariants(pBackgroundExecutor, pServer.getResourceManager(), pProviders)
					.exceptionally(ex -> {
						BaseLogger.log(true, BaseLogLevel.ERROR, "variants failed: " + ex.getMessage());
						return java.util.Collections.emptyMap();
					});

			return controllers.thenCombineAsync(variants, (c, v) -> {
				ResourceCache.Server.CONTROLLERS = c;
				ResourceCache.Server.VARIANTS = v;

				BaseLogger.log(true, BaseLogLevel.INFO, "Variants Cache: " + ResourceCache.Server.VARIANTS);
				BaseLogger.log(true, BaseLogLevel.INFO, "Controllers Cache: " + ResourceCache.Server.CONTROLLERS);
				NetworkRegistry.sendToAllPlayers(pServer, new ControllerCachePacket(ResourceCache.Server.CONTROLLERS));
				return null;
			}, pGameExecutor);
		}

		public static void clearCaches() {
			ResourceCache.Server.VARIANTS = Collections.emptyMap();
			ResourceCache.Server.CONTROLLERS = Collections.emptyMap();
		}
	}
}
