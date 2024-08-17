// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.VariantParameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An interface representing an entity that supports multiple variants. <br>
 * Provides methods to retrieve textures, get random variants, and manage variant lists for entities.
 */
public interface IVariantEntity {

    /**
     * A random source used for generating random variants.
     */
    RandomSource random = RandomSource.create();

    /**
     * Creates a {@link ResourceLocation} for a given mod ID and texture path.
     *
     * @param pModId The mod ID.
     * @param pPath  The texture path.
     * @return A {@link ResourceLocation} pointing to the specified texture.
     */
    default ResourceLocation getTextureLocation(String pModId, String pPath) {
        return new ResourceLocation(pModId, pPath);
    }

    /**
     * Gets the name of the variant associated with this entity.
     *
     * @return The variant name.
     */
    String getVariantName();

    /**
     * Selects a random variant name from the provided list of variant names.
     *
     * @param pVariantNamesList A list of variant names that are available for the entity.
     * @param pDefaultVariant   The default variant name to return if the list is empty.
     * @return A random variant name from the list, or the default variant if the list is empty.
     */
    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        List<String> spawnableVariants = pVariantNamesList.stream().toList();
        if (!spawnableVariants.isEmpty()) {
            return spawnableVariants.get(this.random.nextInt(spawnableVariants.size()));
        }
        return pDefaultVariant;
    }

    /**
     * Retrieves a list of variant names associated with a specific entity name from the given {@link VariantLoader}.
     *
     * @param pEntityName  The name of the entity whose variants are to be retrieved.
     * @return A list of variant names associated with the specified entity.
     */
    default List<String> getEntityVariants(String pEntityName) {
        return VariantLoader.getVariants().stream()
                .filter(variant -> pEntityName.equals(variant.getEntityName()))
                .map(VariantParameter::getVariantName)
                .collect(Collectors.toList());
    }
}
