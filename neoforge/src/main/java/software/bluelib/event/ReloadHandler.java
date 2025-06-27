/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.loader.cache.ResourceCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class ReloadHandler {

	private static final List<IVariantProvider> providers = new ArrayList<>();

	public static void registerProvider(IVariantProvider pProvider) {
		providers.add(pProvider);
	}

	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent pEvent) {
		BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
		BlueLibConstants.server = pEvent.getServer();

		if (providers.isEmpty()) return;
		ResourceCache.registerServerReloadListener(pEvent.getServer(), providers);

		//VariantLoader.loadEntityVariants(pEvent.getServer().getResourceManager(), providers);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.loaded"));
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent pEvent) {
		if (providers.isEmpty()) return;
		ResourceCache.registerServerReloadListener(pEvent.getPlayerList().getServer(), providers);

		//VariantLoader.loadEntityVariants(pEvent.getPlayerList().getServer().getResourceManager(), providers);
		BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variants.reloaded"));
	}
}