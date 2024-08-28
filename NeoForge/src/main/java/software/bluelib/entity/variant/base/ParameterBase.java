// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An {@code Abstract base class} for managing a collection of parameters. <br>
 * This class provides methods to add, retrieve, remove, and list parameters.
 * @author MeAlam
 * @Co-author Dan
 */
public abstract class ParameterBase {

    /**
     * A {@link Map<String>} to store parameters as key-value pairs.
     * @Co-author MeAlam, Dan
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * A {@code void} method to add a parameter to the collection.
     *
     * @param pKey {@link String} - The key under which the parameter is stored.
     * @param pValue {@link Object} - The value of the parameter.
     * @author MeAlam
     * @Co-author Dan
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
     * @Co-author Dan
     */
    protected Object getParameter(String pKey) {
        return parameters.get(pKey);
    }

    /**
     * A {@code Void} that removes a parameter from the collection by its key.
     *
     * @param pKey {@link String} - The key of the parameter to remove.
     * @author MeAlam
     * @Co-author Dan
     */
    protected void removeParameter(String pKey) {
        parameters.remove(pKey);
    }

    /**
     * A {@link Map<Object>} method that returns all parameters in the collection.
     *
     * @return A {@link Map<Object>} containing all parameters.
     * @author MeAlam
     * @Co-author Dan
     */
    protected Map<String, Object> getAllParameters() {
        return new HashMap<>(parameters);
    }

    /**
     *
     * @param pKey
     * @return
     * @author MeAlam
     * @Co-author Dan
     */
    protected boolean containsParameter(String pKey) {
        return parameters.containsKey(pKey);
    }

    /**
     *
     * @return
     * @author MeAlam
     * @Co-author Dan
     */
    protected boolean isEmpty() {
        return parameters.isEmpty();
    }

    /**
     *
     * @author MeAlam
     * @Co-author Dan
     */
    protected void clearParameters() {
        parameters.clear();
    }

    /**
     *
     * @return
     * @author MeAlam
     * @Co-author Dan
     */
    protected int getParameterCount() {
        return parameters.size();
    }

    /**
     *
     * @return
     * @author MeAlam
     * @Co-author Dan
     */
    protected Set<String> getParameterKeys() {
        return parameters.keySet();
    }

    /**
     *
     * @return
     * @author MeAlam
     * @Co-author Dan
     */
    protected Collection<Object> getParameterValues() {
        return parameters.values();
    }

    /**
     *
     * @param pKey
     * @param pNewValue
     * @author MeAlam
     * @Co-author Dan
     */
    protected void updateParameter(String pKey, Object pNewValue) {
        if (parameters.containsKey(pKey)) {
            parameters.put(pKey, pNewValue);
        } else {
            throw new IllegalArgumentException("Key does not exist: " + pKey);
        }
    }

}
