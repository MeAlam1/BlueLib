// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bluelib.entity.variant.base.ParameterBase;

import java.util.Map;
import java.util.Set;

/**
 * Represents the parameters associated with a specific variant of an entity. <br>
 * This class extends {@link ParameterBase} to store and manage variant-specific parameters
 * parsed from a JSON object.
 */
public class VariantParameter extends ParameterBase {

    /**
     * The key in the JSON object that identifies this entity.
     */
    private final String jsonKey;

    /**
     * Constructs a new {@code VariantParameter} instance by extracting parameters from a given JSON object.
     *
     * @param pJsonKey    The key that identifies this entity within the JSON object.
     * @param pJsonObject The JSON object containing the variant parameters.
     */
    public VariantParameter(String pJsonKey, JsonObject pJsonObject) {
        this.jsonKey = pJsonKey;
        Set<Map.Entry<String, JsonElement>> entrySet = pJsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            addParameter(entry.getKey(), entry.getValue().getAsString());
        }
    }

    /**
     * Gets the name of the entity associated with this variant.
     *
     * @return The entity name, which corresponds to the key in the JSON object.
     */
    public String getEntityName() {
        return this.jsonKey;
    }

    /**
     * Gets the name of the variant.
     *
     * @return The variant name, which is stored under the key "Variant".
     */
    public String getVariantName() {
        return (String) getParameter("Variant");
    }

    /**
     * Retrieves a specific parameter associated with this variant.
     *
     * @param pKey The key of the parameter to retrieve.
     * @return The value of the parameter, or {@code null} if the key does not exist.
     */
    public String getParameter(String pKey) {
        return (String) super.getParameter(pKey);
    }
}
