// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant.base;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.VariantParameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A base {@code Interface} providing fundamental methods for handling entity variants.
 * @author MeAlam
 * @Co-author Dan
 */
public interface IVariantEntityBase {

    /**
     * A {@link ResourceLocation} pointing to the texture of an entity.
     *
     * @param pModId {@link String} - The mod ID.
     * @param pPath {@link String} - The texture path.
     * @return A {@link ResourceLocation} pointing to the specified texture.
     * @author MeAlam
     * @Co-author Dan
     */
    default ResourceLocation getTextureLocation(String pModId, String pPath) {
        return new ResourceLocation(pModId, pPath);
    }

    /**
     * A {@link List<String>} of variant names associated with the specified entity.
     *
     * @param pEntityName {@link String} - The name of the entity whose variants are to be retrieved.
     * @return A {@link List<String>} of variant names associated with the specified entity.
     * @author MeAlam
     * @Co-author Dan
     */
    default List<String> getEntityVariants(String pEntityName) {
        List<VariantParameter> variants = VariantLoader.getVariantsFromEntity(pEntityName);
        return variants.stream()
                .map(VariantParameter::getVariantName)
                .collect(Collectors.toList());
    }
}
