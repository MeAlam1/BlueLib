// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bluelib.BlueLib;

public class RexModel extends GeoModel<RexEntity> {


    // Get the Model Location
    @Override
    public ResourceLocation getModelResource(RexEntity pObject) {
        return ResourceLocation.fromNamespaceAndPath(BlueLib.MODID, "geo/rex.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureResource(RexEntity pObject) {
        return pObject.getTextureLocation(BlueLib.MODID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationResource(RexEntity pAnimatable) {
        return ResourceLocation.fromNamespaceAndPath(BlueLib.MODID, "animations/rex.animation.json");
    }
}
