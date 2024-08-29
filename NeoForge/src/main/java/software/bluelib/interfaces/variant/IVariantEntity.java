// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant;

import net.minecraft.util.RandomSource;
import software.bluelib.interfaces.variant.base.IVariantEntityBase;

import java.util.List;

/**
 * An {@code Interface} representing an entity that supports multiple variants.
 * <p>
 * This interface extends {@link IVariantEntityBase} to include methods specific to handling entity variants, including
 * random selection of variants.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getRandomVariant(List, String)} - Retrieves a random variant name from a provided list or defaults if the list is empty.</li>
 * </ul>
 * </p>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public interface IVariantEntity extends IVariantEntityBase {

    /**
     * A {@link RandomSource} instance used for generating random variants.
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    RandomSource random = RandomSource.create();

    /**
     * A {@link String} that selects a random variant name from the provided list of variant names.
     * <p>
     * This method uses the {@link RandomSource} to pick a random variant from the list. If the list is empty, the default
     * variant name is returned.
     * </p>
     *
     * @param pVariantNamesList {@link List<String>} - A {@link List<String>} of variant names available for the entity.
     * @param pDefaultVariant {@link String} - The default variant name to return if {@code pVariantNamesList} is empty.
     * @return A random variant name from the list, or the default variant if the list is empty.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        List<String> spawnableVariants = pVariantNamesList.stream().toList();
        if (!spawnableVariants.isEmpty()) {
            return spawnableVariants.get(this.random.nextInt(spawnableVariants.size()));
        }
        return pDefaultVariant;
    }
}
