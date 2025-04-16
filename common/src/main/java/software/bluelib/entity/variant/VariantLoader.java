// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import software.bluelib.json.JSONParser;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.variant.ParameterUtils;

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
        for (Map.Entry<String, JsonElement> ignored : pJsonObject.entrySet()) {
            AllVariants.putIfAbsent(pEntityName, pJsonObject);
        }
        BaseLogger.log(BaseLogLevel.INFO, "All Entities: " + ParameterUtils.getAllEntities(), true);
        BaseLogger.log(BaseLogLevel.INFO, "Variants of " + pEntityName + ": " + ParameterUtils.getVariantsOfEntity(pEntityName), true);
    }
}
