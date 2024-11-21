// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.fury;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * A {@code public class} that extends {@link GeoEntityRenderer} for rendering the nightfury entity.
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class FuryRender extends GeoEntityRenderer<FuryEntity> {

    /**
     * Constructor
     *
     * @param pRenderManager {@link EntityRendererProvider.Context} - The render manager.
     * @author MeAlam
     * @since 1.0.0
     */
    public FuryRender(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager, new FuryModel());
    }
}
