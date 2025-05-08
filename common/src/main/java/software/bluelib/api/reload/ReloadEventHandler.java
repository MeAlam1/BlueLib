// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.reload;

import com.google.gson.JsonParseException;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;

public class ReloadEventHandler {

    protected static void registerEntityVariants(String pFolderPath, MinecraftServer pServer, String pModID, String pEntityName) {
        try {
            VariantLoader.loadVariants(pFolderPath, pServer, pEntityName);
        } catch (JsonParseException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("json.parse.failed", pEntityName, pModID), pException, true);
            throw pException;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("json.error", pEntityName, pModID), pException, true);
            throw pException;
        }
    }
}
