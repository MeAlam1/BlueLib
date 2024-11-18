// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant;

/**
 * A {@code public Interface} interface responsible for accessing and modifying
 * variant-related properties of an entity within the BlueLib framework.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #setEntityVariantName(String)} - Sets the variant name for an entity.</li>
 *   <li>{@link #getEntityVariantName()} - Retrieves the variant name of an entity.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IVariantAccessor {

    /**
     * A {@code void} method that sets the variant name for the entity.
     *
     * @param pVariantName {@link String} - The variant name to assign to the entity.
     * @author MeAlam
     * @since 1.0.0
     */
    void setEntityVariantName(String pVariantName);

    /**
     * A {@link String} method that retrieves the variant name of the entity.
     *
     * @return {@link String} - The current variant name of the entity.
     * @author MeAlam
     * @since 1.0.0
     */
    String getEntityVariantName();
}
