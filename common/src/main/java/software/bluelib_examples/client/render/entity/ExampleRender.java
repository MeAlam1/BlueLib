
package software.bluelib_examples.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bluelib.loader.renderer.entity.BlueEntityRenderer;
import software.bluelib_examples.client.model.entity.ExampleModel;
import software.bluelib_examples.entity.ExampleEntity;

public class ExampleRender extends BlueEntityRenderer<ExampleEntity> {

    public ExampleRender(EntityRendererProvider.Context context) {
        super(context, new ExampleModel());
    }
}
