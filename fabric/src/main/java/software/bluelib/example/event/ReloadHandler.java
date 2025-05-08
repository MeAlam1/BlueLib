// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.reload.ReloadEventHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class ReloadHandler extends ReloadEventHandler {

    public static void onServerStart(MinecraftServer pServer) {
        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        BlueLibConstants.server = pServer;
        ReloadHandler.LoadEntityVariants(pServer);
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded"), true);
    }

    public static void onReload(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
        if (pServer != null) {
            BlueLibConstants.SCHEDULER.schedule(() -> pServer.execute(() -> {
                ReloadHandler.LoadEntityVariants(pServer);
                BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.reloaded"), true);
            }), 1, TimeUnit.SECONDS);
        }
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
