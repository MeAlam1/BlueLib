// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@code Abstract base class} for managing a collection of parameters. <br>
 * This class provides methods to add, retrieve, remove, and list parameters.
 * @author MeAlam
 */
public abstract class ParameterBase {

    /**
     * A {@link Map<String>} to store parameters as key-value pairs.
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * A {@code void} method to add a parameter to the collection.
     *
     * @param pKey {@link String} - The key under which the parameter is stored.
     * @param pValue {@link Object} - The value of the parameter.
     * @author MeAlam
     */
    protected void addParameter(String pKey, Object pValue) {
        parameters.put(pKey, pValue);
    }

    /**
     * An {@link Object} method to retrieve a parameter from the collection by its key.
     *
     * @param pKey {@link String} - The key of the parameter to retrieve.
     * @return The value associated with the key, or {@code null} if the key does not exist.
     * @author MeAlam
     */
    public Object getParameter(String pKey) {
        return parameters.get(pKey);
    }

    /**
     * A {@code Void} that removes a parameter from the collection by its key.
     *
     * @param pKey {@link String} - The key of the parameter to remove.
     * @author MeAlam
     */
    public void removeParameter(String pKey) {
        parameters.remove(pKey);
    }

    /**
     * A {@link Map<Object>} method that returns all parameters in the collection.
     *
     * @return A {@link Map<Object>} containing all parameters.
     * @author MeAlam
     */
    public Map<String, Object> getAllParameters() {
        return new HashMap<>(parameters);
    }
}
