/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import software.bluelib.client.BlueLibClient;
import software.bluelib.config.ConfigHolder;
import software.bluelib.event.NeoForgeReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.NeoForgeNetworkManager;
import software.bluelib.platform.NeoForgeRegistryHelper;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

	public BlueLib(@NotNull IEventBus pModEventBus, @NotNull ModContainer pModContainer) {
		pModEventBus.addListener(EventPriority.HIGHEST, NeoForgeNetworkManager::registerServerMessages);

		NeoForgeRegistryHelper.register(pModEventBus);

		setupEventListeners(pModEventBus);

		BlueLibCommon.doRegistration();

		if (FMLEnvironment.dist == Dist.CLIENT)
			BlueLibClient.init(pModEventBus, pModContainer);

		registerConfigs(pModContainer);

		NeoForgeReloadHandler.registerProvider(new VariantProvider());
	}

	private void registerConfigs(@NotNull ModContainer pModContainer) {
		pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
		pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
	}

	private void setupEventListeners(@NotNull IEventBus pModEventBus) {
		pModEventBus.register(this);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLoadComplete(@NotNull InterModProcessEvent pEvent) {
		BlueLibCommon.init();
	}
}
