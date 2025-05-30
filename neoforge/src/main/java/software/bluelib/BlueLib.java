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
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.client.BlueLibClient;
import software.bluelib.config.ConfigHolder;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.NeoForgeNetworkManager;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        BlueLibCommon.doServerRegistration();
        ReloadHandler.registerProvider(new VariantProvider());
        pModEventBus.register(this);
        MixinBootstrap.init();
        pModEventBus.addListener(NeoForgeNetworkManager::registerMessages);

        if (FMLEnvironment.dist == Dist.CLIENT)
            BlueLibClient.init(pModContainer);
        
        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
    }

    @SubscribeEvent
    public void onLoadComplete(FMLClientSetupEvent pEvent) {
        BlueLibCommon.init();
    }
}
