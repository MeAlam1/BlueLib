// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.config.ConfigHolder;
import software.bluelib.net.NeoForgeNetworkManager;
import software.bluelib.platform.NeoForgeRegistryHelper;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        BlueLibCommon.doRegistration();
        NeoForgeRegistryHelper.register(pModEventBus, BlueLibConstants.MOD_ID);
        pModEventBus.register(this);
        MixinBootstrap.init();
        pModEventBus.addListener(NeoForgeNetworkManager::registerMessages);
        pModEventBus.addListener(GatherDataEvent.class, this::onGatherData);

        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }

    private void onGatherData(GatherDataEvent event) {
        BlueLibCommon.doDatagen(BlueLibConstants.MOD_ID);
    }
}
