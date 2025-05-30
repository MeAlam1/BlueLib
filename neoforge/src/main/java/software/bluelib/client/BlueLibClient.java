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
import software.bluelib.BlueLibConstants;
import software.bluelib.client.loader.cache.ResourceCache;
import software.bluelib.BlueLibCommon;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BlueLibClient {

	public static void init(ModContainer pModContainer) {
		pModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		BlueLibCommon.doClientRegistration();
		ResourceCache.registerReloadListener();
	}

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers pEvent) {
		BlueLibCommonClient.registerRenderers(pEvent::registerEntityRenderer, pEvent::registerBlockEntityRenderer);
	}
}
