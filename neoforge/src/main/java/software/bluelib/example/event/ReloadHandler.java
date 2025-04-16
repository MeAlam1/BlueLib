// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.BlueLibConstants;
import software.bluelib.event.ReloadEventHandler;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    private static MinecraftServer server;

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent pEvent) {
        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        server = pEvent.getServer();
        ReloadHandler.LoadEntityVariants(server);
        BaseLogger.log(BaseLogLevel.INFO, "Entity variants loaded.", true);
    }

    @SubscribeEvent
    public static void onReload(AddServerReloadListenersEvent pEvent) {
        if (server != null) {
            BlueLibConstants.SCHEDULER.schedule(() -> {
                server.execute(() -> {
                    ReloadHandler.LoadEntityVariants(server);
                    BaseLogger.log(BaseLogLevel.INFO, "Entity variants reloaded.", true);
                });
            }, 1, TimeUnit.SECONDS);
        }
    }

    private static final String basePath = "variant/entity/";

    private static final List<String> ENTITY_NAMES = Arrays.asList("exampleone", "exampletwo");

    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : ENTITY_NAMES) {
            String folderPath = basePath + entityName;
            ReloadEventHandler.registerEntityVariants(folderPath, pServer, BlueLibConstants.MOD_ID, entityName);
            BaseLogger.log(BaseLogLevel.INFO, "Entity variants loaded for " + entityName + ".", true);
        }
    }
}
