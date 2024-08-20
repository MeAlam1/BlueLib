package software.bluetest;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import software.bluelib.event.ReloadEventHandler;
import software.bluetest.event.ClientEvents;
import software.bluetest.init.ModEntities;

@Mod(BlueTest.MODID)
public class BlueTest
{
    public static final String MODID = "bluetest";
    //private static final Logger LOGGER = LogUtils.getLogger();

    public BlueTest(IEventBus pModEventBus)
    {
        //NeoForge.EVENT_BUS.register(this);
        ModEntities.REGISTRY.register(pModEventBus);

        pModEventBus.addListener(ClientEvents::registerAttributes);
        pModEventBus.addListener(ClientEvents::registerRenderers);
        registerEntityVariants();
    }

    private void registerEntityVariants() {
        ReloadEventHandler.registerEntityVariants(MODID, "dragon", "variant/entity/dragon.json", "variant/entity/dragondata.json");
        ReloadEventHandler.registerEntityVariants(MODID, "rex", "variant/entity/rex.json", "variant/entity/rexdata.json");
    }
}
