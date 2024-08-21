package software.bluetest.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.event.ReloadEventHandler;
import software.bluetest.BlueTest;

@Mod.EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    @SubscribeEvent
    public static void onDataPackReload(ServerStartingEvent pEvent) {
        System.out.println("Data packs are being reloaded!");
        ReloadEventHandler.registerEntityVariants(BlueTest.MODID, pEvent, "variant/entity/dragon.json", "variant/entity/dragondata.json");
        ReloadEventHandler.registerEntityVariants(BlueTest.MODID, pEvent, "variant/entity/rex.json", "variant/entity/rexdata.json");
    }
}
