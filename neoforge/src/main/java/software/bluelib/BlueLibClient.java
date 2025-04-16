// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BlueLibConstants.MOD_ID, dist = Dist.CLIENT)
public class BlueLibClient {

    public BlueLibClient(IEventBus pModEventBus, ModContainer pModContainer) {
        pModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
