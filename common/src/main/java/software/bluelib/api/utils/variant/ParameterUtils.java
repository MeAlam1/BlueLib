// Copyright (c) BlueLib. Licensed under the MIT License.

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
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.notfound", pEntityName), true);
            return null;
        }

        return entityData.keySet();
    }

    @Nullable
    public static JsonElement getCustomParameterForVariant(String pEntityName, String pVariantName, String pParameter) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData == null) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.notfound", pEntityName), true);
            return null;
        }

        JsonArray variants = entityData.getAsJsonArray(pVariantName);
        if (variants == null || variants.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("entity.variantsNotfound", pVariantName, pEntityName), true);
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
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("entity.parameterNotfound", pParameter, pVariantName, pEntityName), true);
        return null;
    }
}
