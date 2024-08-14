// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for managing a collection of parameters. <br>
 * This class provides methods to add, retrieve, remove, and list parameters.
 */
public abstract class ParameterBase {

    /**
     * A map to store parameters as key-value pairs.
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * Adds a parameter to the collection.
     *
     * @param pKey   The key under which the parameter is stored.
     * @param pValue The value of the parameter.
     */
    protected void addParameter(String pKey, Object pValue) {
        parameters.put(pKey, pValue);
    }

    /**
     * Retrieves a parameter value by its key.
     *
     * @param pKey The key of the parameter to retrieve.
     * @return The value associated with the key, or {@code null} if the key does not exist.
     */
    public Object getParameter(String pKey) {
        return parameters.get(pKey);
    }

    /**
     * Removes a parameter from the collection by its key.
     *
     * @param pKey The key of the parameter to remove.
     */
    public void removeParameter(String pKey) {
        parameters.remove(pKey);
    }

    /**
     * Retrieves a copy of all parameters as a map.
     *
     * @return A {@link Map} containing all parameters.
     */
    public Map<String, Object> getAllParameters() {
        return new HashMap<>(parameters);
    }
}
