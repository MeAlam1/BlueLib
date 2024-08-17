package software.bluelib.interfaces.variant;

import net.minecraft.util.RandomSource;
import software.bluelib.interfaces.variant.base.IVariantEntityBase;

import java.util.List;

/**
 * An {@code Interface} representing an entity that supports multiple variants. <br>
 * Extends the {@link IVariantEntityBase} to provide entity-specific methods.
 * @author MeAlam
 */
public interface IVariantEntity extends IVariantEntityBase {

    /**
     * A {@link RandomSource} used for generating random variants.
     */
    RandomSource random = RandomSource.create();

    /**
     * A {@link String} that gets the name of the variant associated with this entity.
     *
     * @return The variant name.
     * @author MeAlam
     */
    String getVariantName();

    /**
     * A {@link String} that selects a random variant name from the provided list of variant names.
     *
     * @param pVariantNamesList {@link List<String>} - A {@link List<String>} of variant names that are available for the entity.
     * @param pDefaultVariant {@link String} - The default variant name to return if the list is empty.
     * @return A random variant name from the list, or the default variant if the list is empty.
     * @author MeAlam
     */
    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        List<String> spawnableVariants = pVariantNamesList.stream().toList();
        if (!spawnableVariants.isEmpty()) {
            return spawnableVariants.get(this.random.nextInt(spawnableVariants.size()));
        }
        return pDefaultVariant;
    }
}
