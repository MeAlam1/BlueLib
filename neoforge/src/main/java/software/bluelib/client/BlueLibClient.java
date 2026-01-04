/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.net.NeoForgeNetworkManager;

@Mod(value = BlueLibConstants.MOD_ID, dist = Dist.CLIENT)
public class BlueLibClient {

	public BlueLibClient(@NotNull IEventBus pModEventBus, @NotNull FMLModContainer pModContainer) {
		pModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		pModEventBus.addListener(EventPriority.HIGHEST, NeoForgeNetworkManager::registerClientMessages);
		BlueLibCommon.doClientRegistration();
		ResourceCache.Client.registerReloadListener();
	}

	@SubscribeEvent
	public void registerRenderers(@NotNull final EntityRenderersEvent.RegisterRenderers pEvent) {
		BlueLibCommonClient.registerRenderers(pEvent::registerEntityRenderer, pEvent::registerBlockEntityRenderer);
	}

	@SubscribeEvent
	public void reloadClient(@NotNull AddReloadListenerEvent pEvent) {
		ResourceCache.Client.registerReloadListener();
	}
}
