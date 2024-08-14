package software.bluelib;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import software.bluelib.entity.variant.VariantLoader;

@Mod(BlueLib.MODID)
public class BlueLib
{
    public static final String MODID = "bluelib";
    public static final Logger LOGGER = LogUtils.getLogger();


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
