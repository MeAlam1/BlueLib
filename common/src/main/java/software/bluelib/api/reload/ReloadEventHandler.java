// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.reload;

import com.google.gson.JsonParseException;
import net.minecraft.server.MinecraftServer;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class ReloadEventHandler {

    protected static void registerEntityVariants(String pFolderPath, MinecraftServer pServer, String pModID, String pEntityName) {
        BaseLogger.log(BaseLogLevel.INFO, "Attempting to register entity variants for " + pEntityName + " with ModID: " + pModID, true);

        try {
            VariantLoader.loadVariants(pFolderPath, pServer, pEntityName);
            BaseLogger.log(BaseLogLevel.SUCCESS, "Successfully registered entity variants for " + pEntityName + " from ModID: " + pModID, true);
        } catch (JsonParseException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Failed to parse JSON(s) while registering entity variants for " + pEntityName + " from ModID: " + pModID, pException, true);
            throw pException;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Unexpected error occurred while registering entity variants for " + pEntityName + " from ModID: " + pModID, pException, true);
            throw pException;
        }
    }
}
