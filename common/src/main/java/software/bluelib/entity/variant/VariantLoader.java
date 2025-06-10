/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.entity.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.*;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.json.JSONParser;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.api.utils.variant.ParameterUtils;
import software.bluelib.entity.variant.cache.Variants;
import software.bluelib.internal.Translation;

public class VariantLoader extends JSONParser {

    // entityName -> (variantName -> Variants)
    public static final Map<String, Map<String, Variants>> AllVariants = new HashMap<>();

    private static final VariantLoader LOADER = new VariantLoader();

    public static void loadEntityVariants(ResourceManager pResourceManager, List<IVariantProvider> pProviders) {
        for (IVariantProvider provider : pProviders) {
            List<String> entityNames = provider.getEntityNames();
            String basePath = provider.getBasePath();

            for (String entityName : entityNames) {
                String folderPath = basePath + entityName;
                VariantLoader.loadVariants(folderPath, pResourceManager, entityName);
                BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("variants.loaded.entity", entityName));
            }
        }
    }

    public static void loadVariants(String pFolderPath, ResourceManager pResourceManager, String pEntityName) {
        LOADER.loadData(pFolderPath, pResourceManager);
        parseVariants(pEntityName, LOADER.getMergedJsonObject());
    }

    private static void parseVariants(String pEntityName, JsonObject pVariantsJson) {
        if (BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPre(pEntityName)) {
            BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("variants.load.cancelled"));
            return;
        }

        if (!AllVariants.containsKey(pEntityName)) {
            Map<String, Variants> variantMap = new HashMap<>();
            for (String variantKey : pVariantsJson.keySet()) {
                if (BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPre(variantKey, pEntityName)) {
                    BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("variant.load.cancelled", variantKey, pEntityName));
                    return;
                }
                JsonArray variantArray = pVariantsJson.getAsJsonArray(variantKey);
                if (variantArray != null && !variantArray.isEmpty()) {
                    for (JsonElement variantElement : variantArray) {
                        if (variantElement.isJsonObject()) {
                            Variants record = new Variants(pEntityName, variantKey, variantElement.getAsJsonObject());
                            variantMap.put(variantKey, record);
                        }
                    }
                }
                BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPost(pEntityName, variantKey);
            }
            AllVariants.put(pEntityName, variantMap);
        }

        BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPost(pEntityName);

        BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("variants.entities", Arrays.toString(ParameterUtils.getAllEntities().toArray())));
        BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("variants.variants", pEntityName, Arrays.toString(Objects.requireNonNull(ParameterUtils.getVariantsOfEntity(pEntityName)).toArray())));
    }
}
