/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;

public class ReloadHandler {


    private static final List<IVariantProvider> providers = new ArrayList<>();

    public static void registerProvider(IVariantProvider provider) {
        providers.add(provider);
    }
    public static void onServerStart(MinecraftServer pServer) {
        if (providers.isEmpty()) return;

        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        BlueLibConstants.server = pServer;
        ReloadHandler.LoadEntityVariants(pServer.getResourceManager());
        BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded"));
    }

    public static void onReload(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
        if (providers.isEmpty()) return;

        ReloadHandler.LoadEntityVariants(pServer.getResourceManager());
        BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.reloaded"));
    }

    public static void LoadEntityVariants(ResourceManager pResourceManager) {
        for (IVariantProvider provider : providers) {
            List<String> entityNames = provider.getEntityNames();
            String basePath = provider.getBasePath();

            for (String entityName : entityNames) {
                String folderPath = basePath + entityName;
                VariantLoader.loadVariants(folderPath, pResourceManager, entityName);
                BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded.entity", entityName));
            }
        }
    }
}
