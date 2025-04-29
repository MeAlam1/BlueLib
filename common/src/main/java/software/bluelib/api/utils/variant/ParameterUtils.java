// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Set;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class ParameterUtils {

    private ParameterUtils() {}

    public static Set<String> getAllEntities() {
        Set<String> allEntities = VariantLoader.AllVariants.keySet();
        BaseLogger.log(BaseLogLevel.INFO, "Found Entities: " + allEntities, true);
        return allEntities;
    }

    public static Set<String> getVariantsOfEntity(String pEntityName) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData != null) {
            Set<String> variants = entityData.keySet();
            BaseLogger.log(BaseLogLevel.INFO, "Found Variants: " + variants, true);
            return variants;
        }
        BaseLogger.log(BaseLogLevel.WARNING, "No variants found for: " + pEntityName, true);
        return null;
    }

    public static JsonElement getCustomParameterForVariant(String pEntityName, String pVariantName, String pParameter) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData == null) {
            BaseLogger.log(BaseLogLevel.INFO, "Entity data not found for: " + pEntityName, true);
            return null;
        }

        JsonArray variants = entityData.getAsJsonArray(pVariantName);
        if (variants == null || variants.isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, "Variants not found or empty for: " + pVariantName + " in entity: " + pEntityName, true);
            return null;
        }

        for (JsonElement variantElement : variants) {
            if (variantElement.isJsonObject()) {
                JsonObject variant = variantElement.getAsJsonObject();

                if (variant.has(pParameter)) {
                    JsonElement parameterElement = variant.get(pParameter);

                    if (parameterElement.isJsonPrimitive()) {
                        return parameterElement;
                    } else if (parameterElement.isJsonArray()) {
                        return parameterElement;
                    } else if (parameterElement.isJsonObject()) {
                        return parameterElement;
                    }
                }
            }
        }
        BaseLogger.log(BaseLogLevel.INFO, "Custom parameter: " + pParameter + " not found for: " + pEntityName, true);
        return null;
    }
}
