// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils;

import software.bluelib.entity.variant.VariantParameter;
import software.bluelib.entity.variant.VariantLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class to connect custom parameters to the variants.
 */
public class VariantUtils {

    /**
     * A static map that holds the custom parameters associated with each variant.
     * The outer map's key represents the variant's name, and the inner map stores
     * key-value pairs of custom parameters for that variant.
     * <p>
     * This map is cleared and populated whenever {@link #connectParameters(VariantLoader, Function)} is called.
     */
    private static final Map<String, Map<String, String>> variantParametersMap = new HashMap<>();

    /**
     * Connects custom parameter(s) to the specified {@link software.bluelib.entity.variant.VariantLoader}. <br>
     * <strong>Note:</strong> Currently, only one {@link software.bluelib.entity.variant.VariantLoader} instance is present.
     *
     * @param pLoader The {@link software.bluelib.entity.variant.VariantLoader} instance to which the custom parameters will be connected.
     * @param pICustomParameter A function that maps a {@link VariantParameter} to a {@link Map} containing the custom parameters as key-value pairs.
     */
    public static void connectParameters(VariantLoader pLoader, Function<VariantParameter, Map<String, String>> pICustomParameter) {
        variantParametersMap.clear();
        List<VariantParameter> variants = pLoader.getVariants();
        for (VariantParameter variant : variants) {
            Map<String, String> parameters = pICustomParameter.apply(variant);
            variantParametersMap.put(variant.getVariantName(), parameters);
        }
    }

    /**
     * Retrieves the value of a custom parameter for a specific variant.
     *
     * @param pVariantName The variant name you want to see the custom parameter of.
     * @param pParameterKey The parameter you want to see.
     * @return The value of the custom parameter
     * identified by {@code pParameterKey} for the variant specified by {@code pVariantName}.
     */
    public static String getParameter(String pVariantName, String pParameterKey) {
        return variantParametersMap.getOrDefault(pVariantName, new HashMap<>()).getOrDefault(pParameterKey, "unknown");
    }
}