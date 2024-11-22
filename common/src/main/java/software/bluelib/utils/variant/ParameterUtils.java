// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Set;

/**
 * A {@code public final class} that provides utility methods for managing custom parameters associated with entity variants.
 * <p>
 * This class contains methods to retrieve and handle custom parameters for variants, enabling the building and connecting of
 * parameters to specific variants.
 * </p>
 * <p>
 * <strong>Key Methods:</strong>
 * <ul>
 *   <li>{@link #getAllEntities()} - Retrieves all entities present in the variant loader.</li>
 *   <li>{@link #getVariantsOfEntity(String)} - Retrieves the variants of a specific entity.</li>
 *   <li>{@link #getCustomParameterForVariant(String, String, String)} - Retrieves the value of a custom parameter for a given variant.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.3.0
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

    /**
     * A {@code public static} {@link Set<String>} that retrieves all entities from the variant loader.
     * <p>
     * This method accesses the {@link VariantLoader} to fetch all the entities present in the {@code AllVariants} map.
     * </p>
     *
     * @return A {@link Set<String>} containing all the entity names from the variant loader.
     * @author MeAlam
     * @since 1.3.0
     */
    public static Set<String> getAllEntities() {
        Set<String> allEntities = VariantLoader.AllVariants.keySet();
        BaseLogger.log(BaseLogLevel.INFO, "Found Entities: " + allEntities, true);
        return allEntities;
    }

    /**
     * A {@code public static} {@link Set<String>}  that retrieves all variants for a specific entity.
     * <p>
     * This method checks the provided {@code pEntityName} and returns the corresponding variants from the variant data.
     * </p>
     *
     * @param pEntityName {@link String} - The name of the entity for which variants are to be retrieved.
     * @return A {@link Set<String>} of variant names for the given entity, or {@code null} if the entity does not exist.
     * @author MeAlam
     * @since 1.3.0
     */
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

    /**
     * A {@code public static} {@link String} method that retrieves the value of a custom parameter for a specific variant.
     * <p>
     * This method accesses the variant data for a specified entity and variant and retrieves the value of a custom parameter.
     * </p>
     * <p>
     * The method checks if the parameter exists and returns its value as a {@link String}.
     * </p>
     *
     * @param pEntityName  {@link String} - The name of the entity whose variant parameter is being queried.
     * @param pVariantName {@link String} - The name of the variant whose parameter is being queried.
     * @param pParameter   {@link String} - The name of the custom parameter to retrieve.
     * @return The value of the custom parameter as a {@link String}, or {@code null} if not found.
     * @author MeAlam
     * @since 1.3.0
     */
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
                                String value = parameterElement.getAsString();
                                BaseLogger.log(BaseLogLevel.INFO, "Found custom parameter: " + pParameter + " with value: " + value
                                        + " for: " + pEntityName, true);
                                return value;
                            } else if (parameterElement.isJsonObject()) {
                                String value = parameterElement.getAsString();
                                BaseLogger.log(BaseLogLevel.INFO, "Found custom parameter: " + pParameter + " with value: " + value
                                        + " for: " + pEntityName, true);
                                return value;
                            }
                        }
                    }
                }
            }
        }
        BaseLogger.log(BaseLogLevel.WARNING, "Custom parameter: " + pParameter + " not found for: " + pEntityName, true);
        return null;
    }
}
