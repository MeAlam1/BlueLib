// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.dragon;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * A {@code public class} that extends {@link GeoEntityRenderer} for rendering the dragon entity.
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class DragonRender extends GeoEntityRenderer<DragonEntity> {

    /**
     * Constructor
     *
     * @param pRenderManager {@link EntityRendererProvider.Context} - The render manager.
     * @author MeAlam
     * @since 1.0.0
     */
    public DragonRender(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager, new DragonModel());
    }
}
