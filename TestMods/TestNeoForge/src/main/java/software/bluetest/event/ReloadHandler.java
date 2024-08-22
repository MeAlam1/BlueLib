package software.bluetest.event;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.event.ReloadEventHandler;
import software.bluetest.BlueTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    private static final String BASE_PATH = "variant/entity/";
    private static final List<String> ENTITY_NAMES = Arrays.asList("dragon", "rex");
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static MinecraftServer server;

    @SubscribeEvent
    public static void onDataPackReload(ServerStartingEvent pEvent) {
        server = pEvent.getServer();
        ReloadHandler.LoadEntityVariants(pEvent.getServer());
    }

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

    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : ENTITY_NAMES) {
            String jsonPath = BASE_PATH + entityName + ".json";
            String dataPath = BASE_PATH + entityName + "data.json";
            ReloadEventHandler.registerEntityVariants(pServer, entityName, BlueTest.MODID, jsonPath, dataPath);
        }
    }
}
