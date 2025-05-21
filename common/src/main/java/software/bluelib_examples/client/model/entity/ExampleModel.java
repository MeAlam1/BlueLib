

package software.bluelib_examples.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bluelib_examples.BlueLibConstants;
import software.bluelib_examples.entity.ExampleEntity;

public class ExampleModel extends GeoModel<ExampleEntity> {

    private final ResourceLocation model = ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "geo/example.geo.json");
    private final ResourceLocation animations = ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "animations/example.animation.json");

    @Override
    public ResourceLocation getModelResource(ExampleEntity pExampleEntity, @Nullable GeoRenderer<ExampleEntity> pGeoRenderer) {
        return model;
    }

    @Override
    public ResourceLocation getModelResource(ExampleEntity animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(ExampleEntity pExampleEntity, @Nullable GeoRenderer<ExampleEntity> pGeoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "textures/" + pExampleEntity.entityName + "/" + pExampleEntity.getVariantName() + ".png");
    }

    @Override
    public ResourceLocation getTextureResource(ExampleEntity pExampleEntity) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "textures/" + pExampleEntity.entityName + "/" + pExampleEntity.getVariantName() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(ExampleEntity pExampleEntity) {
        return animations;
    }
}
