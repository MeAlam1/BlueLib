// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(BlueLib.MODID)
public class BlueLib
{
    public static final String MODID = "bluelib";
    //public static final Logger LOGGER = LogUtils.getLogger();


    public BlueLib(IEventBus pModEventBus)
    {
        //NeoForge.EVENT_BUS.register(this);

        if (developerMode()) {
            System.out.println("Thanks for using BlueLib!");
        }
    }

    static boolean developerMode() {
        return !FMLEnvironment.production;
    }
}
