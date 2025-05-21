// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.reload.ReloadEventHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class ReloadHandler extends ReloadEventHandler {

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent pEvent) {
        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        BlueLibConstants.server = pEvent.getServer();
        ReloadHandler.LoadEntityVariants(pEvent.getServer());
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded"), true);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent pEvent) {
        ReloadHandler.LoadEntityVariants(pEvent.getPlayerList().getServer());
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.reloaded"), true);
    }

    private static final String basePath = "variant/entity/";

    private static final List<String> ENTITY_NAMES = Arrays.asList("exampleone", "exampletwo");

    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : ENTITY_NAMES) {
            String folderPath = basePath + entityName;
            ReloadEventHandler.registerEntityVariants(folderPath, pServer, BlueLibConstants.MOD_ID, entityName);
            BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded.entity", entityName), true);
        }
    }
}
