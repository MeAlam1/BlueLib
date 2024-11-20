// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bluelib.entity.variant.base.ParameterBase;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Map;
import java.util.Set;

/**
 * A {@code class} that represents the parameters associated with a specific variant of an entity.
 * <p>
 * This class extends {@link ParameterBase} to store and manage variant-specific parameters parsed from a {@link JsonObject}.
 * <p>
 * The class handles various JSON element types, including {@code JsonPrimitive}, {@code JsonArray}, and {@code JsonObject}.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getJsonKey()} - Retrieves the key of the JSON object that identifies this entity.</li>
 *   <li>{@link #getVariantParameter()} - Retrieves the name of the variant.</li>
 *   <li>{@link #getParameter(String)} - Retrieves the value of a specific parameter by its key.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class VariantParameter extends ParameterBase {

    /**
     * A {@code private final} {@link String} that represents the key of the JSON object that identifies this entity.
     * <p>
     * This key is used to map the entity to its corresponding parameters within a {@link JsonObject}.
     * </p>
     *
     * @since 1.0.0
     */
    private final String jsonKey;

    /**
     * A {@code private static} {@link String} that represents the name of the Variant parameter.
     * <p>
     * This key is used to locate the variant name within the parameters/JSON files.
     * </p>
     *
     * @since 1.0.0
     */
    private static String variantParameterName = "variantName";

    /**
     * Constructs a new {@code VariantParameter} instance by extracting parameters from a given {@link JsonObject}.
     * <p>
     * This constructor processes different types of {@link JsonElement} values:
     * <ul>
     *   <li>{@link com.google.gson.JsonPrimitive}: Stored directly as a string.</li>
     *   <li>{@link com.google.gson.JsonArray}: Converts array elements into a single comma-separated string.</li>
     *   <li>{@link JsonObject}: Converts the nested JSON object to a string representation.</li>
     *   <li>{@code Other Types}: Stores "null" for unhandled JSON types.</li>
     * </ul>
     *
     * @param pJsonKey    {@link String} - The key that identifies this entity within the {@link JsonObject}.
     * @param pJsonObject {@link JsonObject} - The {@link JsonObject} containing the variant parameters.
     * @throws IllegalArgumentException if {@code pJsonKey} or {@code pJsonObject} is {@code null}.
     * @author MeAlam
     * @see ParameterBase
     * @since 1.0.0
     */
    public VariantParameter(String pJsonKey, JsonObject pJsonObject) {
        if (pJsonKey == null || pJsonObject == null) {
            Throwable throwable = new Throwable("JSON key or JSON object is null");
            IllegalArgumentException exception = new IllegalArgumentException("JSON key and object must not be null");
            BaseLogger.log(BaseLogLevel.ERROR, exception.toString(), throwable, true);
            throw exception;
        }
        this.jsonKey = pJsonKey;
        BaseLogger.log(BaseLogLevel.INFO, "Creating VariantParameter with JSON key: " + pJsonKey, true);
        Set<Map.Entry<String, JsonElement>> entryMap = pJsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entryMap) {
            JsonElement element = entry.getValue();
            if (element.isJsonPrimitive()) {
                addParameter(entry.getKey(), element.getAsString());
                BaseLogger.log(BaseLogLevel.SUCCESS, "Added primitive parameter: " + entry.getKey() + " = " + element.getAsString(), true);
            } else if (element.isJsonArray()) {
                StringBuilder arrayValues = new StringBuilder();
                element.getAsJsonArray().forEach(e -> arrayValues.append(e.getAsString()).append(","));
                if (!arrayValues.isEmpty()) {
                    arrayValues.setLength(arrayValues.length() - 1);
                }
                addParameter(entry.getKey(), arrayValues.toString());
                BaseLogger.log(BaseLogLevel.SUCCESS, "Added array parameter: " + entry.getKey() + " = " + arrayValues, true);
            } else if (element.isJsonObject()) {
                addParameter(entry.getKey(), element.toString());
                BaseLogger.log(BaseLogLevel.SUCCESS, "Added object parameter: " + entry.getKey() + " = " + element, true);
            } else {
                addParameter(entry.getKey(), "null");
                BaseLogger.log(BaseLogLevel.SUCCESS, "Added null parameter for key: " + entry.getKey(), true);
            }
        }
    }

    /**
     * A {@link String} method that retrieves the key of the {@link JsonObject} that identifies this entity.
     * <p>
     * This key is used to retrieve or map the entity within a broader data structure.
     * </p>
     *
     * @return The key of the JSON object representing this entity.
     * @throws IllegalStateException if the key is unexpectedly {@code null}.
     * @author MeAlam
     * @since 1.0.0
     */
    public String getJsonKey() {
        if (this.jsonKey == null) {
            Throwable throwable = new Throwable("JSON key should not be null");
            IllegalStateException exception = new IllegalStateException("JSON key is unexpectedly null when retrieving from VariantParameter.");
            BaseLogger.log(BaseLogLevel.ERROR, "JSON key is unexpectedly null when retrieving from VariantParameter.", throwable, true);
            throw exception;
        }
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved JSON key: " + this.jsonKey, true);
        return this.jsonKey;
    }

    /**
     * A {@link String} method that retrieves the name of the variant parameter.
     * <p>
     * The variant name is, by default, stored under the key {@code "variantName"} in the parameters/JSON files.
     * Otherwise, the key is stored in the {@link #variantParameterName} field.
     * </p>
     *
     * @return The name of the variant, or {@code null} if the variant name is not found.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String getVariantParameter() {
        String variantName = variantParameterName;
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved parameter name: " + variantName, true);
        return variantName;
    }

    /**
     * A {@link String} method that retrieves the name of the variant parameter.
     * <p>
     * The variant name is, by default, stored under the key {@code "variantName"} in the parameters/JSON files.
     * Otherwise, the key is stored in the {@link #variantParameterName} field.
     * </p>
     *
     * @return The name of the variant, or {@code null} if the variant name is not found.
     * @author MeAlam
     * @since 1.0.0
     */
    public String filterVariantParameter() {
        String variantName = getParameter(variantParameterName); ;
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved parameter name: " + variantName, true);
        return variantName;
    }

    /**
     * A {@link String} method that sets the name of the variant parameter.
     * <p>
     * This method allows the user to customize the key used to locate the variant name within the parameters/JSON files.
     * </p>
     *
     * @param pCustomVariantName {@link String} - The custom name of the variant parameter.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void setVariantParameter(String pCustomVariantName) {
        variantParameterName = pCustomVariantName;
        BaseLogger.log(BaseLogLevel.INFO, "Setting parameter name: " + variantParameterName, true);
    }

    /**
     * A {@link String} method that retrieves the value of a specific parameter by its key.
     * <p>
     * This method looks up the parameter's value within the internal data structure.
     * </p>
     *
     * @param pKey {@link String} - The key of the parameter to retrieve.
     * @return The value of the parameter, or {@code null} if the key does not exist.
     * @author MeAlam
     * @since 1.0.0
     */
    public String getParameter(String pKey) {
        String value = (String) super.getParameter(pKey);
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved parameter for key " + pKey + ": " + value, true);
        return value;
    }
}
