// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.config.ConfigHolder;
import software.bluelib.test.TestRegistry;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        pModEventBus.register(this);
        TestRegistry.registerTests();
        MixinBootstrap.init();
        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, "bluelib-markdown.toml");
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }
}
