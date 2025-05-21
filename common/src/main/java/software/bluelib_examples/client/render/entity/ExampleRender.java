

package software.bluelib_examples.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bluelib_examples.client.model.entity.ExampleModel;
import software.bluelib_examples.entity.ExampleEntity;

public class ExampleRender extends GeoEntityRenderer<ExampleEntity> {

    public ExampleRender(EntityRendererProvider.Context context) {
        super(context, new ExampleModel());
    }
}
