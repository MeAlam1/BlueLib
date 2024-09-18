// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bluelib.entity.variant.base.ParameterBase;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Map;
import java.util.Set;

/**
 * A {@code VariantParameter} class that represents the parameters associated with a specific variant of an entity.
 * <p>
 * This class extends {@link ParameterBase} to store and manage variant-specific parameters parsed from a JSON object.
 * <p>
 * The class is designed to handle various JSON element types, including {@code JsonPrimitive}, {@code JsonArray}, and {@code JsonObject}.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getJsonKey()} - Retrieves the key of the JSON object that identifies this entity.</li>
 *   <li>{@link #getVariantName()} - Retrieves the name of the variant.</li>
 *   <li>{@link #getParameter(String)} - Retrieves the value of a specific parameter by its key.</li>
 * </ul>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class VariantParameter extends ParameterBase {

    /**
     * A {@link String} that represents the key of the JSON object that identifies this entity.
     * <p>
     * This key is used to map the entity to its corresponding parameters within a {@link JsonObject}.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private final String jsonKey;

    /**
     * Constructs a new {@code VariantParameter} instance by extracting parameters from a given JSON object.
     * <p>
     * This constructor processes different types of {@link JsonElement} values:
     * <ul>
     *   <li>{@code JsonPrimitive}: Stored directly as a string.</li>
     *   <li>{@code JsonArray}: Converts array elements into a single comma-separated string.</li>
     *   <li>{@code JsonObject}: Converts the nested JSON object to a string representation.</li>
     *   <li>{@code Other Types}: Stores "null" for unhandled JSON types.</li>
     * </ul>
     * </p>
     * @param pJsonKey {@link String} - The key that identifies this entity within the {@link JsonObject}.
     * @param pJsonObject {@link JsonObject} - The {@link JsonObject} containing the variant parameters.
     * @throws IllegalArgumentException if {@code pJsonKey} or {@code pJsonObject} is null.
     * @see ParameterBase
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public VariantParameter(String pJsonKey, JsonObject pJsonObject) {
        if (pJsonKey == null || pJsonObject == null) {
            Throwable throwable = new Throwable("JSON key or JSON object is null");
            IllegalArgumentException exception = new IllegalArgumentException("JSON key and object must not be null");
            BaseLogger.logError(exception.toString(), throwable);
            throw exception;
        }
        this.jsonKey = pJsonKey;
        BaseLogger.bluelibLogInfo("Creating VariantParameter with JSON key: " + pJsonKey);
        Set<Map.Entry<String, JsonElement>> entryMap = pJsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entryMap) {
            JsonElement element = entry.getValue();
            if (element.isJsonPrimitive()) {
                addParameter(entry.getKey(), element.getAsString());
                BaseLogger.bluelibLogInfo("Added primitive parameter: " + entry.getKey() + " = " + element.getAsString());
            } else if (element.isJsonArray()) {
                StringBuilder arrayValues = new StringBuilder();
                element.getAsJsonArray().forEach(e -> arrayValues.append(e.getAsString()).append(","));
                if (!arrayValues.isEmpty()) {
                    arrayValues.setLength(arrayValues.length() - 1);
                }
                addParameter(entry.getKey(), arrayValues.toString());
                BaseLogger.bluelibLogInfo("Added array parameter: " + entry.getKey() + " = " + arrayValues.toString());
            } else if (element.isJsonObject()) {
                addParameter(entry.getKey(), element.toString());
                BaseLogger.bluelibLogInfo("Added object parameter: " + entry.getKey() + " = " + element.toString());
            } else {
                addParameter(entry.getKey(), "null");
                BaseLogger.bluelibLogInfo("Added null parameter for key: " + entry.getKey());
            }
        }
    }

    /**
     * A {@link String} method that returns the key of the {@link JsonObject} that identifies this entity.
     * <p>
     * This key is typically used to retrieve or map the entity within a broader data structure.
     * </p>
     * @return The key of the JSON object representing this entity.
     * @throws IllegalStateException if the key is unexpectedly null.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public String getJsonKey() {
        if (this.jsonKey == null) {
            Throwable throwable = new Throwable("JSON key should not be null");
            IllegalStateException exception = new IllegalStateException("JSON key is unexpectedly null when retrieving from VariantParameter.");
            BaseLogger.logError(exception.toString(), throwable);
            throw exception;
        }
        BaseLogger.bluelibLogSuccess("Retrieved JSON key: " + this.jsonKey);
        return this.jsonKey;
    }

    /**
     * A {@link String} method that retrieves the name of the variant.
     * <p>
     * The variant name is expected to be stored under the key {@code "variantName"} in the parameters/JSON Files.
     * </p>
     * @return The name of the variant, or {@code null} if the variant name is not found.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public String getVariantName() {
        String variantName = getParameter("variantName");
        BaseLogger.bluelibLogSuccess("Retrieved variant name: " + variantName);
        return variantName;
    }

    /**
     * A {@link String} method that retrieves the value of a specific parameter by its key.
     * <p>
     * This method looks up the parameter's value within the internal data structure.
     * </p>
     * @param pKey {@link String} - The key of the parameter to retrieve.
     * @return The value of the parameter, or {@code null} if the key does not exist.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public String getParameter(String pKey) {
        String value = (String) super.getParameter(pKey);
        BaseLogger.bluelibLogSuccess("Retrieved parameter for key " + pKey + ": " + value);
        return value;
    }
}
