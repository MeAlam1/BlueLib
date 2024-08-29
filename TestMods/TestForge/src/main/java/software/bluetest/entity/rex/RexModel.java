// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest.entity.rex;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bluetest.BlueTest;

public class RexModel extends GeoModel<RexEntity> {


    // Get the Model Location
    @Override
    public ResourceLocation getModelResource(RexEntity pObject) {
        return new ResourceLocation(BlueTest.MODID, "geo/rex.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureResource(RexEntity pObject) {
        return pObject.getTextureLocation(BlueTest.MODID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationResource(RexEntity pAnimatable) {
        return new ResourceLocation(BlueTest.MODID, "animations/rex.animation.json");
    }
}
