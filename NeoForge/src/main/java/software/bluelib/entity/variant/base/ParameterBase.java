// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import software.bluelib.utils.logging.BaseLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An abstract base class for managing a collection of parameters.
 * <p>
 * This class provides methods to add, retrieve, remove, and manipulate parameters stored as key-value pairs.
 * </p>
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
     * <p>
     * This map holds parameter keys and their corresponding values.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * A {@code void} that adds a parameter to the collection.
     * <p>
     * This method stores a new parameter with the specified key and value in the internal map.
     * </p>
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
     * A {@link Object} that retrieves a parameter from the collection by its key.
     * <p>
     * This method returns the value associated with the specified key, or {@code null} if the key does not exist.
     * </p>
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
     * A {@code void} that removes a parameter from the collection by its key.
     * <p>
     * This method deletes the parameter with the specified key from the internal map. If the key does not exist, no action is taken.
     * </p>
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
     * A {@link Map<String>} that returns all parameters in the collection.
     * <p>
     * This method returns a new {@link Map} containing all parameters stored in the internal map.
     * </p>
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
     * A {@link Boolean} that checks if a parameter exists by its key.
     * <p>
     * This method returns {@code true} if the parameter with the specified key exists in the collection, {@code false} otherwise.
     * </p>
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
     * A {@link Boolean} that checks if the collection of parameters is empty.
     * <p>
     * This method returns {@code true} if the collection contains no parameters, {@code false} otherwise.
     * </p>
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
     * A {@code void} that clears all parameters from the collection.
     * <p>
     * This method removes all parameters from the internal map.
     * </p>
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected void clearParameters() {
        parameters.clear();
        BaseLogger.bluelibLogSuccess("Cleared all parameters.");
    }

    /**
     * A {@link Integer} that returns the number of parameters in the collection.
     * <p>
     * This method provides the count of parameters currently stored in the internal map.
     * </p>
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
     * A {@link Set<String>} that returns a set of all parameter keys.
     * <p>
     * This method provides a {@link Set} containing all the keys of parameters in the collection.
     * </p>
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
     * A {@link Collection<Object>} that returns a collection of all parameter values.
     * <p>
     * This method provides a {@link Collection} containing all the values of parameters in the collection.
     * </p>
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
     * A {@code void} that updates the value of an existing parameter.
     * <p>
     * This method changes the value of a parameter identified by the specified key. If the key does not exist, an exception is thrown.
     * </p>
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
