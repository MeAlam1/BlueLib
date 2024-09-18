// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import software.bluelib.utils.logging.BaseLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An {@code Abstract base class} for managing a collection of parameters.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #addParameter(String, Object)} - Adds a parameter to the collection.</li>
 *   <li>{@link #getParameter(String)} - Retrieves a parameter from the collection.</li>
 *   <li>{@link #removeParameter(String)} - Removes a parameter from the collection.</li>
 *   <li>{@link #getAllParameters()} - Returns all parameters in the collection.</li>
 *   <li>{@link #containsParameter(String)} - Checks if a parameter exists by its key.</li>
 *   <li>{@link #isEmpty()} - Checks if the collection of parameters is empty.</li>
 *   <li>{@link #clearParameters()} - Clears all parameters from the collection.</li>
 *   <li>{@link #getParameterCount()} - Returns the number of parameters in the collection.</li>
 *   <li>{@link #getParameterKeys()} - Returns a set of all parameter keys.</li>
 *   <li>{@link #getParameterValues()} - Returns a collection of all parameter values.</li>
 *   <li>{@link #updateParameter(String, Object)} - Updates the value of an existing parameter.</li>
 * </ul>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public abstract class ParameterBase {

    /**
     * A {@link Map} to store parameters as key-value pairs.
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * A {@code void} method to add a parameter to the collection.
     *
     * @param pKey {@link String} - The key under which the parameter is stored.
     * @param pValue {@link Object} - The value of the parameter.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected void addParameter(String pKey, Object pValue) {
        parameters.put(pKey, pValue);
        BaseLogger.bluelibLogSuccess(String.format("Parameter added: Key = %s, Value = %s", pKey, pValue));
    }

    /**
     * An {@link Object} method to retrieve a parameter from the collection by its key.
     *
     * @param pKey {@link String} - The key of the parameter to retrieve.
     * @return The value associated with the key, or {@code null} if the key does not exist.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected Object getParameter(String pKey) {
        Object value = parameters.get(pKey);
        BaseLogger.bluelibLogSuccess(String.format("Parameter retrieved: Key = %s, Value = %s", pKey, value));
        return value;
    }

    /**
     * A {@code void} method that removes a parameter from the collection by its key.
     *
     * @param pKey {@link String} - The key of the parameter to remove.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected void removeParameter(String pKey) {
        if (parameters.remove(pKey) != null) {
            BaseLogger.bluelibLogSuccess(String.format("Parameter removed: Key = %s", pKey));
        } else {
            BaseLogger.logWarning(String.format("Attempted to remove non-existent parameter: Key = %s", pKey));
        }
    }

    /**
     * A {@link Map} method that returns all parameters in the collection.
     *
     * @return A {@link Map} containing all parameters.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected Map<String, Object> getAllParameters() {
        BaseLogger.bluelibLogSuccess("Retrieved all parameters.");
        return new HashMap<>(parameters);
    }

    /**
     * A {@link Boolean} method that checks if a parameter exists by its key.
     *
     * @param pKey {@link String} - The key of the parameter to check.
     * @return {@code true} if the parameter exists, {@code false} otherwise.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected boolean containsParameter(String pKey) {
        boolean exists = parameters.containsKey(pKey);
        BaseLogger.bluelibLogInfo(String.format("Parameter existence check: Key = %s, Exists = %b", pKey, exists));
        return exists;
    }

    /**
     * A {@link Boolean} method that checks if the collection of parameters is empty.
     *
     * @return {@code true} if the collection is empty, {@code false} otherwise.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected boolean isEmpty() {
        boolean empty = parameters.isEmpty();
        BaseLogger.bluelibLogInfo("Checked if parameters are empty: " + empty);
        return empty;
    }

    /**
     * A {@code void} method that clears all parameters from the collection.
     *
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected void clearParameters() {
        parameters.clear();
        BaseLogger.bluelibLogSuccess("Cleared all parameters.");
    }

    /**
     * An {@link Integer} method that returns the number of parameters in the collection.
     *
     * @return The number of parameters in the collection.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected int getParameterCount() {
        int count = parameters.size();
        BaseLogger.bluelibLogSuccess("Retrieved parameter count: " + count);
        return count;
    }

    /**
     * A {@link Set} method that returns a set of all parameter keys.
     *
     * @return A {@link Set} containing all parameter keys.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected Set<String> getParameterKeys() {
        BaseLogger.bluelibLogSuccess("Retrieved parameter keys.");
        return parameters.keySet();
    }

    /**
     * A {@link Collection} method that returns a collection of all parameter values.
     *
     * @return A {@link Collection} containing all parameter values.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected Collection<Object> getParameterValues() {
        BaseLogger.bluelibLogSuccess("Retrieved parameter values.");
        return parameters.values();
    }

    /**
     * A {@code void} method that updates the value of an existing parameter.
     *
     * @param pKey {@link String} - The key of the parameter to update.
     * @param pNewValue {@link Object} - The new value to set for the parameter.
     * @throws IllegalArgumentException if the key does not exist.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected void updateParameter(String pKey, Object pNewValue) {
        if (parameters.containsKey(pKey)) {
            parameters.put(pKey, pNewValue);
            BaseLogger.bluelibLogInfo(String.format("Parameter updated: Key = %s, New Value = %s", pKey, pNewValue));
        } else {
            Throwable throwable = new Throwable("Key does not exist: " + pKey);
            IllegalArgumentException exception = new IllegalArgumentException("Key does not exist: " + pKey);
            BaseLogger.logError(String.format("Attempted to update non-existent parameter: Key = %s", pKey), throwable);
            throw exception;
        }
    }
}
