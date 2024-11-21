// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.fury;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bluelib.BlueLibConstants;

/**
 * A {@code public class} that extends {@link GeoModel} for the {@link FuryEntity} entity.
 * Key Methods:
 * <ul>
 *   <li>{@link #getModelResource(FuryEntity, GeoRenderer)} - Get the Model Location.</li>
 *   <li>{@link #getTextureResource(FuryEntity, GeoRenderer)} - Get the Texture Location.</li>
 *   <li>{@link #getAnimationResource(FuryEntity)} - Get the Animation Location.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class FuryModel extends GeoModel<FuryEntity> {


    /**
     * A {@code public} {@link ResourceLocation} method that returns the location of the model.
     *
     * @param pObject      {@link FuryEntity} - The entity to get the model for.
     * @param pGeoRenderer {@link GeoRenderer} - The renderer for the entity.
     * @return {@link ResourceLocation} - The location of the model.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public ResourceLocation getModelResource(FuryEntity pObject, @Nullable GeoRenderer<FuryEntity> pGeoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "geo/nightfury.geo.json");
    }

    /**
     * A {@code public} {@link ResourceLocation} method that returns the location of the texture.
     *
     * @param pObject      {@link FuryEntity} - The entity to get the texture for.
     * @param pGeoRenderer {@link GeoRenderer} - The renderer for the entity.
     * @return {@link ResourceLocation} - The location of the texture.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public ResourceLocation getTextureResource(FuryEntity pObject, @Nullable GeoRenderer<FuryEntity> pGeoRenderer) {
        return pObject.getTextureLocation(BlueLibConstants.MOD_ID, "geo/nightfury.png");
    }

    /**
     * A {@code public} {@link ResourceLocation} method that returns the location of the animation.
     *
     * @param pAnimatable {@link FuryEntity} - The entity to get the animation for.
     * @return {@link ResourceLocation} - The location of the animation.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public ResourceLocation getAnimationResource(FuryEntity pAnimatable) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "animations/dragon.animation.json");
    }
}
