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
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.internal.BlueTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ReloadHandler {


	private static final List<IVariantProvider> providers = new ArrayList<>();

	public static void registerProvider(IVariantProvider provider) {
		providers.add(provider);
	}

	public static void onServerStart(MinecraftServer pServer) {
		BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
		BlueLibConstants.server = pServer;
		
		if (providers.isEmpty()) return;
		VariantLoader.loadEntityVariants(pServer.getResourceManager(), providers);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.loaded"));
	}

	public static void onReload(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
		if (providers.isEmpty()) return;

		VariantLoader.loadEntityVariants(pServer.getResourceManager(), providers);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.reloaded"));
	}
}
