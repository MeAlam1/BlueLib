/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;

@SuppressWarnings("unused")
public class ParameterUtils {

    private ParameterUtils() {}

    @NotNull
    public static Set<String> getAllEntities() {
        return VariantLoader.AllVariants.keySet();
    }

    @Nullable
    public static Set<String> getVariantsOfEntity(String pEntityName) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.notfound", pEntityName));
            return null;
        }

        return entityData.keySet();
    }

    @Nullable
    public static JsonElement getParameterDataForVariant(String pEntityName, String pVariantName, String pParameter, @NotNull String pFallbackVariant) {
        JsonElement result = getParameterDataForVariant(pEntityName, pVariantName, pParameter);
        if (result == null && !pFallbackVariant.equals(pVariantName)) {
            result = getParameterDataForVariant(pEntityName, pFallbackVariant, pParameter);
        }
        return result;
    }

    @Nullable
    public static JsonElement getParameterDataForVariant(String pEntityName, String pVariantName, String pParameter) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.notfound", pEntityName));
            return null;
        }

        JsonArray variants = entityData.getAsJsonArray(pVariantName);
        if (variants == null || variants.isEmpty()) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.variantsNotfound", pVariantName, pEntityName));
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
        BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("entity.parameterNotfound", pParameter, pVariantName, pEntityName));
        return null;
    }
}
