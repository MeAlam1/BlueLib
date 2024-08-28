package software.bluetest.event;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.event.ReloadEventHandler;
import software.bluetest.BlueTest;
import software.bluetest.init.ModEntities;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    private static MinecraftServer server;
    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent pEvent) {
        server = pEvent.getServer();
        ReloadHandler.LoadEntityVariants(server);
    }

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent pEvent) {
        if (server != null) {
            scheduler.schedule(() -> {
                server.execute(() -> {
                    ReloadHandler.LoadEntityVariants(server);
                });
            }, 1, TimeUnit.SECONDS);
        }
    }

    private static final String basePath = "variant/entity/";
    private static final List<String> entityNames = Arrays.asList("dragon", "rex");
    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : entityNames) {
            String modPath = basePath + entityName + ".json";
            String dataPath = basePath + entityName + "data.json";
            ReloadEventHandler.registerEntityVariants(pServer, entityName, BlueTest.MODID, modPath, dataPath);
        }
    }
}
