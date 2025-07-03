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
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class NeoForgeReloadHandler {

	public static void registerProvider(@NotNull IVariantProvider pProvider) {
		ReloadHandler.registerProvider(pProvider);
	}

	@SubscribeEvent
	public static void onServerStart(@NotNull ServerStartingEvent pEvent) {
		ReloadHandler.onServerStart(pEvent.getServer());
	}

	@SubscribeEvent
	public static void onDatapackSync(@NotNull OnDatapackSyncEvent pEvent) {
		ReloadHandler.onReload(pEvent.getPlayerList().getServer());
	}
}
