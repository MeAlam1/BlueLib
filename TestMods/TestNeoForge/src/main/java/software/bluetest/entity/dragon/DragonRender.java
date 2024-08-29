// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest.entity.dragon;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DragonRender extends GeoEntityRenderer<DragonEntity> {

    // Render the entity
    public DragonRender(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager, new DragonModel());
    }
}
