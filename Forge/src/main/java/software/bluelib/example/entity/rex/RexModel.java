// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bluelib.BlueLib;

public class RexModel extends AnimatedGeoModel<RexEntity> {


    // Get the Model Location
    @Override
    public ResourceLocation getModelLocation(RexEntity pObject) {
        return new ResourceLocation(BlueLib.MODID, "geo/rex.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureLocation(RexEntity pObject) {
        return pObject.getTextureLocation(BlueLib.MODID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationFileLocation(RexEntity pAnimatable) {
        return new ResourceLocation(BlueLib.MODID, "animations/rex.animation.json");
    }
}
