// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant;

import software.bluelib.entity.variant.VariantParameter;

import java.util.Map;

/**
 * An {@code Interface} for defining custom parameters associated with a specific {@link VariantParameter}. <br>
 * Implementations of this interface should provide a method to retrieve custom key-value pairs
 * that are tied to a particular variant.
 */
public interface ICustomParameter {

    /**
     * A {@link Map<String>} that retrieves a {@link Map<String>} of custom parameters for the specified variant.
     *
     * @param pVariant {@link VariantParameter} - The {@link VariantParameter} for which custom parameters are to be retrieved.
     * @return A {@link Map<String>} containing custom parameters as key-value pairs, where the key is the parameter name
     * and the value is the parameter's value.
     */
    Map<String, String> getCustomParameters(VariantParameter pVariant);
}
