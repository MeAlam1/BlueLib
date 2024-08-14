package software.bluetest.entity.dragon;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bluetest.BlueTest;

public class DragonModel extends GeoModel<DragonEntity> {

    // Get the Model Location
    @Override
    public ResourceLocation getModelResource(DragonEntity pObject) {
        return new ResourceLocation(BlueTest.MODID, "geo/dragon.geo.json");
    }

    // Get the Texture Location
    @Override
    public ResourceLocation getTextureResource(DragonEntity pObject) {
        return pObject.getTextureLocation(BlueTest.MODID, "textures/entity/" + pObject.getEntityName() + "/" + pObject.getVariantName() + ".png");
    }

    // Get the Animation Location
    @Override
    public ResourceLocation getAnimationResource(DragonEntity pAnimatable) {
        return new ResourceLocation(BlueTest.MODID, "animations/dragon.animation.json");
    }
}
