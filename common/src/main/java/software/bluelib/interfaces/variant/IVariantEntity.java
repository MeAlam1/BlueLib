// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant;

import java.util.List;
import net.minecraft.util.RandomSource;
import software.bluelib.interfaces.variant.base.IVariantEntityBase;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public Interface} representing an entity that supports multiple variants.
 * <p>
 * This interface extends {@link IVariantEntityBase} to include methods specific to handling entity variants, including
 * random selection of variants.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #getRandomVariant(List, String)} - Retrieves a random variant name from a provided list or defaults if the list is empty.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface IVariantEntity extends IVariantEntityBase {

    /**
     * A {@link RandomSource} instance used for generating random variants.
     *
     * @since 1.0.0
     */
    RandomSource random = RandomSource.create();

    /**
     * A {@code default} {@link String} that selects a random variant name from the provided list of variant names.
     * <p>
     * This method uses the {@link RandomSource} to pick a random variant from the list. If the list is empty, the default
     * variant name is returned.
     * </p>
     *
     * @param pVariantNamesList {@link List<String>} - A {@link List<String>} of variant names available for the entity.
     * @param pDefaultVariant   {@link String} - The default variant name to return if {@code pVariantNamesList} is empty.
     * @return A random variant name from the list, or the default variant if the list is empty.
     * @author MeAlam
     * @since 1.0.0
     */
    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        if (pVariantNamesList.isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, "Variant names list is empty. Returning default variant: " + pDefaultVariant, true);
            return pDefaultVariant;
        }
        int index = random.nextInt(pVariantNamesList.size());
        String selectedVariant = pVariantNamesList.get(index);
        BaseLogger.log(BaseLogLevel.SUCCESS, "Selected random variant: " + selectedVariant + " from list of size: " + pVariantNamesList.size(), true);
        return selectedVariant;
    }
}
