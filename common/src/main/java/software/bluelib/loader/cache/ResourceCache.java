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
import java.util.concurrent.Executor;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.loader.BlueLoader;
import software.bluelib.loader.cache.variants.EntityCache;

public class ResourceCache extends BlueLoader {

	@NotNull
	public static Map<ResourceLocation, EntityCache> VARIANTS = Collections.emptyMap();

	@NotNull
	public static Map<ResourceLocation, EntityCache> getVariants() {
		return VARIANTS;
	}

	public static void registerServerReloadListener(@NotNull MinecraftServer pServer, @NotNull List<IVariantProvider> pProviders) {
		ResourceCache.reloadServer(pProviders, pServer.getResourceManager(), Util.backgroundExecutor(), pServer);
	}

	public static CompletableFuture<Void> reloadServer(
			@NotNull List<IVariantProvider> pProviders,
			@NotNull ResourceManager pResourceManager,
			@NotNull Executor pBackgroundExecutor,
			@NotNull Executor pGameExecutor) {
		clearServerCaches();

		CompletableFuture<Map<ResourceLocation, EntityCache>> variants = loadVariants(pBackgroundExecutor, pResourceManager, pProviders);

		return CompletableFuture.allOf(variants)
				.thenRunAsync(() -> {
					ResourceCache.VARIANTS = variants.join();

					BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("loader.variants.loaded"));
				}, pGameExecutor);
	}

	public static void clearServerCaches() {
		ResourceCache.VARIANTS = Collections.emptyMap();
	}
}
