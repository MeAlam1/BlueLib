// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A {@code public abstract base class} for managing a collection of {@link #parameters}.
 * <p>
 * This {@code class} provides methods to add, retrieve, remove, and manipulate {@link #parameters} stored as key-value pairs.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #addParameter(String, Object)} - Adds a parameter to {@link #parameters}.</li>
 *   <li>{@link #getParameter(String)} - Retrieves a parameter from {@link #parameters}.</li>
 *   <li>{@link #removeParameter(String)} - Removes a parameter from {@link #parameters}.</li>
 *   <li>{@link #getAllParameters()} - Returns all parameters in {@link #parameters}.</li>
 *   <li>{@link #containsParameter(String)} - Checks if a parameter exists by its key from {@link #parameters}.</li>
 *   <li>{@link #isEmpty()} - Checks if {@link #parameters} is empty.</li>
 *   <li>{@link #clearParameters()} - Clears all parameters from {@link #parameters}.</li>
 *   <li>{@link #getParameterCount()} - Returns the number of parameters in {@link #parameters}.</li>
 *   <li>{@link #getParameterKeys()} - Returns a set of all parameter keys from {@link #parameters}.</li>
 *   <li>{@link #getParameterValues()} - Returns a collection of all parameter values from {@link #parameters}.</li>
 *   <li>{@link #updateParameter(String, Object)} - Updates the value of an existing parameter in {@link #parameters}.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public abstract class ParameterBase {

    /**
     * A {@code private final} {@link Map<String>} to store parameters as key-value pairs.
     * <p>
     * This {@link Map<String>} holds parameter keys and their corresponding values.
     * </p>
     *
     * @co-author MeAlam, Dan
     * @since 1.0.0
     */
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * A {@code protected void} that adds a parameter to {@link #parameters}.
     * <p>
     * This method stores a new parameter with the specified key and value in {@link #parameters}.
     * </p>
     *
     * @param pKey   {@link String} - The key under which the parameter is stored.
     * @param pValue {@link Object} - The value of the parameter.
     * @author MeAlam
     * @since 1.0.0
     */
    protected void addParameter(String pKey, Object pValue) {
        parameters.put(pKey, pValue);
        BaseLogger.log(BaseLogLevel.SUCCESS, String.format("Parameter added: Key = %s, Value = %s", pKey, pValue), true);
    }

    /**
     * A {@code protected} {@link Object} that retrieves a parameter from {@link #parameters} by its key.
     * <p>
     * This method returns the value associated with the specified key, or {@code null} if the key does not exist.
     * </p>
     *
     * @param pKey {@link String} - The key of the parameter to retrieve.
     * @return {@link Object} - The value associated with the key, or {@code null} if the key does not exist.
     * @author MeAlam
     * @since 1.0.0
     */
    protected Object getParameter(String pKey) {
        Object value = parameters.get(pKey);
        BaseLogger.log(BaseLogLevel.INFO, String.format("Parameter retrieved: Key = %s, Value = %s", pKey, value), true);
        return value;
    }

    /**
     * A {@code protected void} that removes a parameter from {@link #parameters} by its key.
     * <p>
     * This method deletes the parameter with the specified key from {@link #parameters}. If the key does not exist, no action is taken.
     * </p>
     *
     * @param pKey {@link String} - The key of the parameter to remove.
     * @author MeAlam
     * @since 1.0.0
     */
    protected void removeParameter(String pKey) {
        if (parameters.remove(pKey) != null) {
            BaseLogger.log(BaseLogLevel.SUCCESS, String.format("Parameter removed: Key = %s", pKey), true);
        } else {
            BaseLogger.log(BaseLogLevel.WARNING, String.format("Attempted to remove non-existent parameter: Key = %s", pKey), true);
        }
    }

    /**
     * A {@code protected} {@link Map<String>} that returns all parameters in {@link #parameters}.
     * <p>
     * This method returns a new {@link Map<String>} containing all parameters stored in {@link #parameters}.
     * </p>
     *
     * @return {@link Map<String>} - A {@link Map<String>} containing all parameters.
     * @author MeAlam
     * @since 1.0.0
     */
    protected Map<String, Object> getAllParameters() {
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved all parameters.", true);
        return new HashMap<>(parameters);
    }

    /**
     * A {@code protected} {@link Boolean} that checks if a parameter exists by its key.
     * <p>
     * This method returns {@code true} if the parameter with the specified key exists in {@link #parameters}, {@code false} otherwise.
     * </p>
     *
     * @param pKey {@link String} - The key of the parameter to check.
     * @return {@link Boolean} - {@code true} if the parameter exists and {@code false} if it doesn't.
     * @author MeAlam
     * @since 1.0.0
     */
    protected boolean containsParameter(String pKey) {
        boolean exists = parameters.containsKey(pKey);
        BaseLogger.log(BaseLogLevel.INFO, String.format("Parameter existence check: Key = %s, Exists = %b", pKey, exists), true);
        return exists;
    }

    /**
     * A {@code protected} {@link Boolean} that checks if {@link #parameters} is empty.
     * <p>
     * This method returns {@code true} if {@link #parameters} contains no parameters, {@code false} otherwise.
     * </p>
     *
     * @return {@link Boolean} - {@code true} if {@link #parameters} is empty and {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    protected boolean isEmpty() {
        boolean empty = parameters.isEmpty();
        BaseLogger.log(BaseLogLevel.INFO, "Checked if parameters are empty: " + empty, true);
        return empty;
    }

    /**
     * A {@code protected void} that removes all parameters from {@link #parameters}.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    protected void clearParameters() {
        parameters.clear();
        BaseLogger.log(BaseLogLevel.SUCCESS, "Cleared all parameters.", true);
    }

    /**
     * A {@code protected} {@link Integer} that returns the number of parameters in {@link #parameters}.
     *
     * @return {@link Integer} - The number of parameters in the collection.
     * @author MeAlam
     * @since 1.0.0
     */
    protected int getParameterCount() {
        int count = parameters.size();
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved parameter count: " + count, true);
        return count;
    }

    /**
     * A {@code protected} {@link Set<String>} that returns a set of all parameter keys.
     * <p>
     * This method provides a {@link Set<String>} containing all the keys of parameters in {@link #parameters}.
     * </p>
     *
     * @return {@link Set<String>} - A {@link Set<String>} containing all parameter keys.
     * @author MeAlam
     * @since 1.0.0
     */
    protected Set<String> getParameterKeys() {
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved parameter keys.", true);
        return parameters.keySet();
    }

    /**
     * A {@code protected} {@link Collection<Object>} that returns a {@link Collection<Object>} of all parameter values.
     * <p>
     * This method provides a {@link Collection<Object>} containing all the values of parameters in {@link #parameters}.
     * </p>
     *
     * @return {@link Collection<Object>} - A {@link Collection<Object>} containing all parameter values.
     * @author MeAlam
     * @since 1.0.0
     */
    protected Collection<Object> getParameterValues() {
        BaseLogger.log(BaseLogLevel.INFO, "Retrieved parameter values.", true);
        return parameters.values();
    }

    /**
     * A {@code protected void} that updates the value of an existing parameter.
     * <p>
     * This method changes the value of a parameter in {@link #parameters} that is identified by the specified key. If the key does not exist, an exception is thrown.
     * </p>
     *
     * @param pKey      {@link String} - The key of the parameter to update.
     * @param pNewValue {@link Object} - The new value to set for the parameter.
     * @throws IllegalArgumentException if the key does not exist.
     * @author MeAlam
     * @since 1.0.0
     */
    protected void updateParameter(String pKey, Object pNewValue) {
        if (parameters.containsKey(pKey)) {
            parameters.put(pKey, pNewValue);
            BaseLogger.log(BaseLogLevel.SUCCESS, String.format("Parameter updated: Key = %s, New Value = %s", pKey, pNewValue), true);
        } else {
            Throwable throwable = new Throwable("Key does not exist: " + pKey);
            IllegalArgumentException exception = new IllegalArgumentException("Key does not exist: " + pKey);
            BaseLogger.log(BaseLogLevel.ERROR, String.format("Attempted to update non-existent parameter: Key = %s", pKey), throwable, true);
            throw exception;
        }
    }
}
