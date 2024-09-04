// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RexRender extends GeoEntityRenderer<RexEntity> {
    // Render the entity
    public RexRender(EntityRendererManager pRenderManager) {
        super(pRenderManager, new RexModel());
    }
}
