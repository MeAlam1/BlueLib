// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;

import software.bluelib.entity.variant.VariantLoader;

/**
 * A utility class for managing custom parameters associated with entity variants.
 * <p>
 * Provides methods to retrieve custom parameters for variants and allows for
 * building and connecting parameters to specific variants via the {} class.
 * </p>
 * <p>
 * <strong>Key Methods:</strong>
 * <ul>
 * </ul>
 * <p>
 *
 * @author MeAlam
 * @version 1.0.0
 * @see VariantLoader
 * @since 1.0.0
 */
public class ParameterUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private ParameterUtils() {
    }

    public static Set<String> getAllEntities() {
        return VariantLoader.AllVariants.keySet();
    }

    public static Set<String> getVariantsOfEntity(String pEntityName) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData != null) {
            return entityData.keySet();
        }
        return null;
    }

    public static String getCustomParameterForVariant(String pEntityName, String pVariantName, String pParameter) {
        JsonObject entityData = VariantLoader.AllVariants.get(pEntityName);
        if (entityData != null) {
            JsonArray variants = entityData.getAsJsonArray(pVariantName);

            if (variants != null && !variants.isEmpty()) {
                for (int i = 0; i < variants.size(); i++) {
                    JsonElement variantElement = variants.get(i);
                    if (variantElement.isJsonObject()) {
                        JsonObject variant = variantElement.getAsJsonObject();
                        if (variant.has(pParameter)) {
                            JsonElement parameterElement = variant.get(pParameter);
                            if (parameterElement.isJsonPrimitive()) {
                                return parameterElement.getAsString();
                            } else if (parameterElement.isJsonObject()) {
                                return parameterElement.getAsString();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
