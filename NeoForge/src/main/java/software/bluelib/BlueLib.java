package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(BlueLib.MODID)
public class BlueLib
{
    public static final String MODID = "bluelib";
    //private static final Logger LOGGER = LogUtils.getLogger();


    public BlueLib(IEventBus pModEventBus)
    {
        //NeoForge.EVENT_BUS.register(this);

        if (developerMode()) {
            // Only In Development Mode!
        }
    }

    static boolean developerMode() {
        return !FMLEnvironment.production;
    }
}
