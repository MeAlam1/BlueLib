// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant.base;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.utils.variant.ParameterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
 * @version 1.3.0
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
        return new ArrayList<>(Objects.requireNonNull(ParameterUtils.getVariantsOfEntity(pEntityName)));
    }
}
