// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.spongepowered.asm.launch.MixinBootstrap;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        pModEventBus.register(this);
        MixinBootstrap.init();
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }
}
