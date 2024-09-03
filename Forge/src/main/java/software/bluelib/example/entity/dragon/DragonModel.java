// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.dragon;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bluelib.BlueLib;

public class DragonModel extends AnimatedGeoModel<DragonEntity> {


    // Get the Model Location
    @Override
    public ResourceLocation getModelLocation(DragonEntity pObject) {
        return new ResourceLocation(BlueLib.MODID, "geo/dragon.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureLocation(DragonEntity pObject) {
        return pObject.getTextureLocation(BlueLib.MODID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationFileLocation(DragonEntity pAnimatable) {
        return new ResourceLocation(BlueLib.MODID, "animations/dragon.animation.json");
    }
}
