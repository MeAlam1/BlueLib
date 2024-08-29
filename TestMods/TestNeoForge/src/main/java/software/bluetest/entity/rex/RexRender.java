// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest.entity.rex;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RexRender extends GeoEntityRenderer<RexEntity> {

    // Render the entity
    public RexRender(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager, new RexModel());
    }
}
