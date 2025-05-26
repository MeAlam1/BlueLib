/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.variant;

import com.google.gson.JsonElement;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.Variants;

@SuppressWarnings("unused")
public class ParameterUtils {

    private ParameterUtils() {}

    @NotNull
    public static Set<String> getAllEntities() {
        return VariantLoader.AllVariants.keySet();
    }

    @Nullable
    public static Set<String> getVariantsOfEntity(String pEntityName) {
        Map<String, Variants> entityVariants = VariantLoader.AllVariants.get(pEntityName);
        if (entityVariants == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.notfound", pEntityName));
            return null;
        }
        return entityVariants.keySet();
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
        Map<String, Variants> entityVariants = VariantLoader.AllVariants.get(pEntityName);
        if (entityVariants == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.notfound", pEntityName));
            return null;
        }
        Variants record = entityVariants.get(pVariantName);
        if (record == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.variantsNotfound", pVariantName, pEntityName));
            return null;
        }
        JsonElement parameterElement = record.getParameter(pParameter);
        if (parameterElement == null) {
            BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("entity.parameterNotfound", pParameter, pVariantName, pEntityName));
        }
        return parameterElement;
    }
}
