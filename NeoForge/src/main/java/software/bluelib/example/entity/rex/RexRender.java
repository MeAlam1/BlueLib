// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * A {@code public class} that extends {@link GeoEntityRenderer} for rendering the rex entity.
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class RexRender extends GeoEntityRenderer<RexEntity> {

    /**
     * Constructor
     *
     * @param pRenderManager {@link EntityRendererProvider.Context} - The render manager.
     * @author MeAlam
     * @since 1.0.0
     */
    public RexRender(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager, new RexModel());
    }
}
