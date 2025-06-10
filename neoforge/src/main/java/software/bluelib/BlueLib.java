/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.client.BlueLibClient;
import software.bluelib.config.ConfigHolder;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.NeoForgeNetworkManager;
import software.bluelib.platform.NeoForgeRegistryHelper;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {
    private static final Logger LOGGER = LogManager.getLogger();

    /*public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        BlueLibCommon.doRegistration();
        ReloadHandler.registerProvider(new VariantProvider());
        NeoForgeRegistryHelper.register(pModEventBus);
        pModEventBus.register(this);
        MixinBootstrap.init();
        pModEventBus.addListener(NeoForgeNetworkManager::registerMessages);
        pModEventBus.addListener(GatherDataEvent.class, this::onGatherData);

        if (FMLEnvironment.dist == Dist.CLIENT)
            BlueLibClient.init(pModContainer);

        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
    }*/
	public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
		BlueLibCommon.doRegistration();
		NeoRegistries.register(pModEventBus);

		if (FMLEnvironment.dist == Dist.CLIENT)
			BlueLibClient.init(pModContainer);

		registerConfigs(pModContainer);
		setupEventListeners(pModEventBus);

		ReloadHandler.registerProvider(new VariantProvider());
	}

	private void registerConfigs(ModContainer pModContainer) {
		pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
		pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
	}

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }

    private void onGatherData(GatherDataEvent event) {
        LOGGER.info("Starting data generation for mod {}", BlueLibConstants.MOD_ID);
        AbstractRegistryBuilder.doDatagen();
        LOGGER.info("Data providers registered");
    }
	private void setupEventListeners(IEventBus pModEventBus) {
		pModEventBus.register(this);
		pModEventBus.addListener(NeoForgeNetworkManager::registerMessages);
        pModEventBus.addListener(GatherDataEvent.class, this::onGatherData);
	}

	@SubscribeEvent
	public void onLoadComplete(FMLClientSetupEvent pEvent) {
		BlueLibCommon.init();
	}
}
