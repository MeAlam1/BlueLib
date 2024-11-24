// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bluelib.BlueLibConstants;

/**
 * A {@code public class} that extends {@link GeoModel} for the {@link RexEntity} entity.
 * Key Methods:
 * <ul>
 * <li>{@link #getModelResource(RexEntity, GeoRenderer)} - Get the Model Location.</li>
 * <li>{@link #getTextureResource(RexEntity, GeoRenderer)} - Get the Texture Location.</li>
 * <li>{@link #getAnimationResource(RexEntity)} - Get the Animation Location.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class RexModel extends GeoModel<RexEntity> {

    /**
     * A {@code public} {@link ResourceLocation} method that returns the location of the model.
     *
     * @param pObject      {@link RexEntity} - The entity to get the model for.
     * @param pGeoRenderer {@link GeoRenderer} - The renderer for the entity.
     * @return {@link ResourceLocation} - The location of the model.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public ResourceLocation getModelResource(RexEntity pObject, @Nullable GeoRenderer<RexEntity> pGeoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "geo/rex.geo.json");
    }

    /**
     * A {@code public} {@link ResourceLocation} method that returns the location of the texture.
     *
     * @param pObject      {@link RexEntity} - The entity to get the texture for.
     * @param pGeoRenderer {@link GeoRenderer} - The renderer for the entity.
     * @return {@link ResourceLocation} - The location of the texture.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public ResourceLocation getTextureResource(RexEntity pObject, @Nullable GeoRenderer<RexEntity> pGeoRenderer) {
        return pObject.getTextureLocation(BlueLibConstants.MOD_ID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    /**
     * A {@code public} {@link ResourceLocation} method that returns the location of the animation.
     *
     * @param pAnimatable {@link RexEntity} - The entity to get the animation for.
     * @return {@link ResourceLocation} - The location of the animation.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public ResourceLocation getAnimationResource(RexEntity pAnimatable) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "animations/rex.animation.json");
    }
}
