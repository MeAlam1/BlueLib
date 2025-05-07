// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.api.utils.variant.ParameterUtils;
import software.bluelib.json.JSONParser;

public class VariantLoader extends JSONParser {

    public static final Map<String, JsonObject> AllVariants = new HashMap<>();

    private static final VariantLoader LOADER = new VariantLoader();

    public static void loadVariants(String pFolderPath, MinecraftServer pServer, String pEntityName) {
        LOADER.loadData(pFolderPath, pServer);
        AllVariants.putAll(LOADER.getDataMap());
        parseVariants(pEntityName, LOADER.getMergedJsonObject());
        BaseLogger.log(BaseLogLevel.INFO, "All data of Variants: " + AllVariants, true);
    }

    private static void parseVariants(String pEntityName, JsonObject pJsonObject) {
        if (BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPre(pEntityName)) {
            BaseLogger.log(BaseLogLevel.INFO, "Loading all the Variants has been cancelled.", true);
            return;
        }
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            String key = entry.getKey();
            if (BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPre(key, pEntityName)) {
                BaseLogger.log(BaseLogLevel.INFO, "Loading variant: " + key + " of entity: " + pEntityName + " has been cancelled.", true);
                return;
            }
            if (!AllVariants.containsKey(pEntityName)) {
                AllVariants.put(pEntityName, pJsonObject);
                BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPost(pEntityName, key);
            }
        }
        BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPost(pEntityName);
        BaseLogger.log(BaseLogLevel.INFO, "All Entities: " + ParameterUtils.getAllEntities(), true);
        BaseLogger.log(BaseLogLevel.INFO, "Variants of " + pEntityName + ": " + ParameterUtils.getVariantsOfEntity(pEntityName), true);
    }
}
