// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.dragon;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bluelib.BlueLib;

public class DragonModel extends GeoModel<DragonEntity> {


    // Get the Model Location
    @Override
    public ResourceLocation getModelResource(DragonEntity pObject) {
        return ResourceLocation.fromNamespaceAndPath(BlueLib.MODID, "geo/dragon.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureResource(DragonEntity pObject) {
        return pObject.getTextureLocation(BlueLib.MODID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationResource(DragonEntity pAnimatable) {
        return ResourceLocation.fromNamespaceAndPath(BlueLib.MODID, "animations/dragon.animation.json");
    }
}
