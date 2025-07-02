/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.loader.cache.ResourceCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ReloadHandler {

	@NotNull
	private static final List<IVariantProvider> providers = new ArrayList<>();

	public static void registerProvider(@NotNull IVariantProvider provider) {
		providers.add(provider);
	}

	public static void onServerStart(@NotNull MinecraftServer pServer) {
		BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
		BlueLibConstants.server = pServer;
		if (providers.isEmpty()) return;
		ResourceCache.registerServerReloadListener(pServer, providers);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.loaded"));
	}

	public static void onReload(@NotNull MinecraftServer pServer, @NotNull CloseableResourceManager pCloseableResourceManager, @NotNull Boolean pBoolean) {
		if (providers.isEmpty()) return;
		ResourceCache.registerServerReloadListener(pServer, providers);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.reloaded"));
	}
}
