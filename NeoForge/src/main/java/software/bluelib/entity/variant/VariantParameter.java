// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bluelib.entity.variant.base.ParameterBase;

import java.util.Map;
import java.util.Set;

/**
 * A {@code Class} that represents the parameters associated with a specific variant of an entity. <br>
 * This class extends {@link ParameterBase} to store and manage variant-specific parameters
 * parsed from a JSON object.
 */
public class VariantParameter extends ParameterBase {

    /**
     * A {@link String} that represents the key of the JSON object that identifies this entity.
     */
    private final String jsonKey;

    /**
     * Constructs a new {@code VariantParameter} instance by extracting parameters from a given JSON object.
     *
     * @param pJsonKey {@link String} - The key that identifies this entity within the {@link JsonObject}.
     * @param pJsonObject {@link JsonObject} - The {@link JsonObject} containing the variant parameters.
     */
    public VariantParameter(String pJsonKey, JsonObject pJsonObject) {
        this.jsonKey = pJsonKey;
        Set<Map.Entry<String, JsonElement>> entrySet = pJsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            addParameter(entry.getKey(), entry.getValue().getAsString());
        }
    }

    /**
     * A {@link String} that represents the key of the {@link JsonObject} that identifies this entity.
     *
     * @return The entity name, which corresponds to the key in the {@link JsonObject}.
     */
    public String getEntityName() {
        return this.jsonKey;
    }

    /**
     * A {@link String} that represents the name of the variant.
     *
     * @return The variant name, which is stored under the key {@code "Variant"}.
     */
    public String getVariantName() {
        return getParameter("Variant");
    }

    /**
     * A {@link String} that represents the name of the variant.
     *
     * @param pKey {@link String} - The key of the parameter to retrieve.
     * @return The value of the parameter, or {@code null} if the key does not exist.
     */
    public String getParameter(String pKey) {
        return (String) super.getParameter(pKey);
    }
}
