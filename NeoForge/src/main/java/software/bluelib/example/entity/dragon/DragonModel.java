// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.dragon;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bluelib.BlueLibConstants;
import software.bluelib.utils.logging.BaseLogger;

public class DragonModel extends GeoModel<DragonEntity> {


    // Get the Model Location
    @Override
    public ResourceLocation getModelResource(DragonEntity pObject) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "geo/dragon.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureResource(DragonEntity pObject) {
        return pObject.getTextureLocation(BlueLibConstants.MOD_ID, "textures/entity/" + pObject.entityName + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationResource(DragonEntity pAnimatable) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "animations/dragon.animation.json");
    }
}
