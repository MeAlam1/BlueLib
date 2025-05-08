// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibCommon;
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
    }

    private static void parseVariants(String pEntityName, JsonObject pJsonObject) {
        if (BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPre(pEntityName)) {
            BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.load.cancelled"), true);
            return;
        }
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            String key = entry.getKey();
            if (BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPre(key, pEntityName)) {
                BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variant.load.cancelled", key, pEntityName), true);
                return;
            }
            if (!AllVariants.containsKey(pEntityName)) {
                AllVariants.put(pEntityName, pJsonObject);
                BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPost(pEntityName, key);
            }
        }
        BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPost(pEntityName);
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.entities", Arrays.toString(ParameterUtils.getAllEntities().toArray())), true);
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.variants", pEntityName, Arrays.toString(Objects.requireNonNull(ParameterUtils.getVariantsOfEntity(pEntityName)).toArray())), true);
    }
}
