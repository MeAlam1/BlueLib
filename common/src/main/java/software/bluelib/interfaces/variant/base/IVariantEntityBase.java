// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant.base;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.VariantParameter;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@code public base Interface} providing fundamental methods for handling entity variants.
 * <p>
 * This interface defines methods for retrieving texture locations and variant names associated with entities.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getTextureLocation(String, String)} - Retrieves the {@link ResourceLocation} for the entity texture.</li>
 *   <li>{@link #getEntityVariants(String)} - Retrieves a {@link List<String>} of variant names for a specified entity.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public interface IVariantEntityBase {

    /**
     * A {@code default} {@link ResourceLocation} that points to the texture of an entity.
     * <p>
     * This method constructs a {@link ResourceLocation} using the provided mod ID and texture path.
     * </p>
     *
     * @param pModId {@link String} - The mod ID used to locate the texture.
     * @param pPath  {@link String} - The path to the texture within the mod.
     * @return A {@link ResourceLocation} pointing to the specified texture.
     * @author MeAlam
     * @since 1.0.0
     */
    default ResourceLocation getTextureLocation(String pModId, String pPath) {
        return ResourceLocation.fromNamespaceAndPath(pModId, pPath);
    }

    /**
     * A {@code default} {@link List<String>} of variant names associated with the specified entity.
     * <p>
     * This method retrieves the names of all variants for a given entity by querying the {@link VariantLoader}.
     * </p>
     *
     * @param pEntityName {@link String} - The name of the entity whose variant names are to be retrieved.
     * @return A {@link List<String>} containing the names of variants associated with the specified entity.
     * @author MeAlam
     * @since 1.0.0
     */
    default List<String> getEntityVariants(String pEntityName) {
        List<VariantParameter> variants = VariantLoader.getVariantsFromEntity(pEntityName);
        List<String> variantNames = variants.stream()
                .map(VariantParameter::getVariantParameter)
                .collect(Collectors.toList());
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved " + variantNames.size() + " variants for entity: " + pEntityName);
        return variantNames;
    }
}
