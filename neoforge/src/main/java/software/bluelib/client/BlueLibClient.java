/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.loader.cache.ResourceCache;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT)
public class BlueLibClient {

	public static void init(@NotNull ModContainer pModContainer) {
		pModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		BlueLibCommon.doClientRegistration();
		ResourceCache.registerClientReloadListener();
	}

	@SubscribeEvent
	public static void registerRenderers(@NotNull final EntityRenderersEvent.RegisterRenderers pEvent) {
		BlueLibCommonClient.registerRenderers(pEvent::registerEntityRenderer, pEvent::registerBlockEntityRenderer);
	}

	@SubscribeEvent
	public static void reloadClient(@NotNull AddReloadListenerEvent pEvent) {
		ResourceCache.registerClientReloadListener();
	}
}
