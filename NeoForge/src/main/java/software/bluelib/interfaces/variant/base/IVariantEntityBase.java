package software.bluelib.interfaces.variant.base;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.VariantParameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A base {@code Interface} providing fundamental methods for handling entity variants.
 */
public interface IVariantEntityBase {

    /**
     * A {@link ResourceLocation} pointing to the texture of an entity.
     *
     * @param pModId {@link String} - The mod ID.
     * @param pPath {@link String} - The texture path.
     * @return A {@link ResourceLocation} pointing to the specified texture.
     * @author MeAlam
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
     */
    default List<String> getEntityVariants(String pEntityName) {
        return VariantLoader.getVariants().stream()
                .filter(variant -> pEntityName.equals(variant.getEntityName()))
                .map(VariantParameter::getVariantName)
                .collect(Collectors.toList());
    }
}
