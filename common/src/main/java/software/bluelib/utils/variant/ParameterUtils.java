// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.variant;

import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.VariantParameter;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A utility class for managing custom parameters associated with entity variants.
 * <p>
 * Provides methods to retrieve custom parameters for variants and allows for
 * building and connecting parameters to specific variants via the {@link ParameterBuilder} class.
 * </p>
 * <p>
 * <strong>Key Methods:</strong>
 * <ul>
 *   <li>{@link #getParameter(String, String)} - Retrieves the value of a custom parameter for a specific variant.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Nested Classes:</strong>
 * <ul>
 *   <li>{@link ParameterBuilder} - Builder class for creating and associating custom parameters with variants.</li>
 * </ul>
 * </p>
 *
 * @since 1.0.0
 * @author MeAlam
 * @Co-author Dan
 * @version 1.0.0
 * @see software.bluelib.entity.variant.VariantParameter
 */
public class ParameterUtils {

    /**
     * Holds custom parameters for each variant.
     * <p>
     * The outer map's key is the variant name, and the inner map contains key-value pairs
     * representing custom parameters for that variant.
     * </p>
     *
     * @since 1.0.0
     * @Co-author MeAlam, Dan
     */
    private static final Map<String, Map<String, String>> variantParametersMap = new HashMap<>();

    /**
     * A {@link String} that retrieves the value of a custom parameter for a specific variant.
     * <p>
     * If the parameter is not found, {@code null} is returned.
     * </p>
     *
     * @param pVariantName {@link String} The name of the variant.
     * @param pParameterKey {@link String} The key of the parameter to retrieve.
     * @return {@link String} The value of the custom parameter for the specified variant or {@code null}  if not found.
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public static String getParameter(String pVariantName, String pParameterKey) {
        return variantParametersMap.getOrDefault(pVariantName, new HashMap<>()).getOrDefault(pParameterKey, "null");
    }

    /**
     * A {@code class} for creating and associating custom parameters with a specific variant.
     * <p>
     * Allows chaining methods to build and connect parameters to a variant.
     * </p>
     * <p>
     * <strong>Key Methods:</strong>
     * <ul>
     *   <li>{@link #forVariant(String, String)} - Creates a new instance of {@link ParameterBuilder} for a specific entity and variant.</li>
     *   <li>{@link #withParameter(String)} - Adds a parameter with a default value of {@code null} .</li>
     *   <li>{@link #connect()} - Connects the parameters to the variant and updates {@link VariantParameter} with the parameters.</li>
     * </ul>
     * </p>
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public static class ParameterBuilder {

        /**
         * The name of the variant being associated with custom parameters.
         *
         * @since 1.0.0
         * @Co-author MeAlam, Dan
         */
        private final String variantName;

        /**
         * The name of the entity being associated with custom parameters.
         *
         * @since 1.0.0
         * @Co-author MeAlam, Dan
         */
        private final String entityName;

        /**
         * Stores custom parameters being built for the variant.
         *
         * @since 1.0.0
         * @Co-author MeAlam, Dan
         */
        private final Map<String, String> parameters = new HashMap<>();

        /**
         * Constructor to initialize the builder for a specific entity and variant.
         *
         * @param pEntityName {@link String} The name of the entity.
         * @param pVariantName {@link String} The name of the variant.
         * @since 1.0.0
         * @author MeAlam
         * @Co-author Dan
         */
        private ParameterBuilder(String pEntityName, String pVariantName) {
            this.variantName = pVariantName;
            this.entityName = pEntityName;
        }

        /**
         * A {@link ParameterBuilder} that creates a new instance of {@link ParameterBuilder} for the specified entity and variant.
         *
         * @param pEntityName {@link String} The name of the entity.
         * @param pVariantName {@link String} The name of the variant.
         * @return {@link ParameterBuilder} A new instance for chaining.
         * @since 1.0.0
         * @author MeAlam
         * @Co-author Dan
         */
        public static ParameterBuilder forVariant(String pEntityName, String pVariantName) {
            return new ParameterBuilder(pEntityName, pVariantName);
        }

        /**
         * A {@link ParameterBuilder} that adds a custom parameter to the builder with a default value of "null".
         * <p>
         * The {@code null}  value is used if the parameter is not specified in the data source.
         * </p>
         *
         * @param pParameter {@link String} The parameter key.
         * @return {@link ParameterBuilder} The builder instance for chaining.
         * @since 1.0.0
         * @author MeAlam
         * @Co-author Dan
         */
        public ParameterBuilder withParameter(String pParameter) {
            parameters.put(pParameter, "null");
            return this;
        }

        /**
         * A {@link ParameterBuilder} that connects the custom parameters to the specified variant and updates the {@link VariantParameter}.
         * <p>
         * Throws a {@link NoSuchElementException} if the variant or entity is not found.
         * </p>
         *
         * @return {@link ParameterBuilder} The builder instance for chaining.
         * @throws NoSuchElementException if the variant or entity is not found in the database.
         * @since 1.0.0
         * @author MeAlam
         * @Co-author Dan
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
                Throwable cause = new Throwable("Variant or entity not found in the database");
                BaseLogger.log(BaseLogLevel.ERROR, "Variant '" + variantName + "' not found for entity '" + entityName + "'", cause);
            }
            return this;
        }
    }
}
