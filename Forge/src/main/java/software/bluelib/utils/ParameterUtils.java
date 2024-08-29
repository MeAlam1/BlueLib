// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils;

import software.bluelib.entity.variant.VariantParameter;
import software.bluelib.entity.variant.VariantLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A {@code Class} for managing custom parameters associated with entity variants.
 * <p>
 * This class provides methods to retrieve custom parameters for variants and allows
 * for building and connecting parameters to specific variants using the {@link ParameterBuilder}.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getParameter(String, String)} - Retrieves the value of a custom parameter for a specific variant.</li>
 * </ul>
 * </p>
 * Nested Classes:
 * <ul>
 *   <li>{@link ParameterBuilder} - A builder class for creating and associating custom parameters with a specific variant.</li>
 * </ul>
 * </p>
 * @since 1.0.0
 * @author MeAlam
 * @Co-author Dan
 */
public class ParameterUtils {

    /**
     * A {@link Map<String>} holding custom parameters for each variant.
     * <p>
     * The outer map's key is the variant's name, and the inner map contains key-value pairs
     * of custom parameters for that variant.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private static final Map<String, Map<String, String>> variantParametersMap = new HashMap<>();

    /**
     * Retrieves the value of a custom parameter for a specific variant.
     * <p>
     * If the parameter is not found, "null" is returned.
     * </p>
     *
     * @param pVariantName {@link String} - The name of the variant.
     * @param pParameterKey {@link String} - The key of the parameter to retrieve.
     * @return The value of the custom parameter for the specified variant.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public static String getParameter(String pVariantName, String pParameterKey) {
        return variantParametersMap.getOrDefault(pVariantName, new HashMap<>()).getOrDefault(pParameterKey, "null");
    }

    /**
     * A {@code Builder} class for creating and associating custom parameters with a specific variant.
     * <p>
     * This class allows chaining methods to build and connect parameters to a variant.
     * </p>
     *
     * <p>
     * Key Methods:
     * <ul>
     *   <li>{@link #forVariant(String, String)} - Creates a new instance of {@link ParameterBuilder} for the specified entity and variant.</li>
     *   <li>{@link #withParameter(String)} - Adds a parameter to the parameters map with a default value of "null".</li>
     *   <li>{@link #connect()} - Adds parameters to the variant and updates the static {@link VariantParameter} with these parameters.</li>
     * </ul>
     * </p>
     * <p>
     * **Note:** The "null" value is used only if the parameter is not specified in the JSON files.
     * </p>
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public static class ParameterBuilder {
        /**
         * The name of the variant for which parameters are being built.
         * @Co-author MeAlam, Dan
         * @since 1.0.0
         */
        private final String variantName;

        /**
         * The name of the entity for which parameters are being built.
         * @Co-author MeAlam, Dan
         * @since 1.0.0
         */
        private final String entityName;

        /**
         * A {@link Map<String, String>} to store parameters for the variant.
         * <p>
         * Each key-value pair represents a parameter name and its default value.
         * </p>
         * @Co-author MeAlam, Dan
         * @since 1.0.0
         */
        private final Map<String, String> parameters = new HashMap<>();

        /**
         * Constructor to initialize the builder with a specific entity name and variant name.
         *
         * @param pEntityName {@link String} - The name of the entity.
         * @param pVariantName {@link String} - The name of the variant.
         * @author MeAlam
         * @Co-author Dan
         * @since 1.0.0
         */
        private ParameterBuilder(String pEntityName, String pVariantName) {
            this.variantName = pVariantName;
            this.entityName = pEntityName;
        }

        /**
         * Creates a new instance of {@link ParameterBuilder} for the specified entity and variant.
         *
         * @param pEntityName {@link String} - The name of the entity.
         * @param pVariantName {@link String} - The name of the variant.
         * @return A new instance of {@link ParameterBuilder}.
         * @author MeAlam
         * @Co-author Dan
         * @since 1.0.0
         */
        public static ParameterBuilder forVariant(String pEntityName, String pVariantName) {
            return new ParameterBuilder(pEntityName, pVariantName);
        }

        /**
         * Adds a parameter to the parameters map with a default value of "null". <br>
         * <p>
         * **Note:** The "null" value is used only if the parameter is not specified in the JSON files.
         * </p>
         *
         * @param pParameter {@link String} - The key of the parameter to add.
         * @return The current instance of {@link ParameterBuilder} for method chaining.
         * @author MeAlam
         * @Co-author Dan
         * @since 1.0.0
         */
        public ParameterBuilder withParameter(String pParameter) {
            parameters.put(pParameter, "null");
            return this;
        }

        /**
         * Adds a parameter to the parameters map with a specified default value.
         * <p>
         * Connects the parameters built with this builder to the specified variant.
         * Updates the static {@link VariantParameter} with the parameters for the specified variant.
         * </p>
         *
         * @return The current instance of {@link ParameterBuilder} for method chaining.
         * @throws NoSuchElementException if the specified variant is not found for the entity.
         * @author MeAlam
         * @Co-author Dan
         * @since 1.0.0
         */
        public ParameterBuilder connect() {
            VariantParameter variant = VariantLoader.getVariantByName(entityName, variantName);
            if (variant != null) {
                Map<String, String> updatedParameters = new HashMap<>();
                for (String key : parameters.keySet()) {
                    updatedParameters.put(key, variant.getParameter(key));
                }
                variantParametersMap.put(variantName, updatedParameters);
            } else {
                throw new NoSuchElementException("Variant '" + variantName + "' not found for entity '" + entityName + "'");
            }
            return this;
        }
    }
}
