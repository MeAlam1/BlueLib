// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.dragon;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DragonRender extends GeoEntityRenderer<DragonEntity> {

    // Render the entity
    public DragonRender(EntityRendererManager pRenderManager) {
        super(pRenderManager, new DragonModel());
    }
}
