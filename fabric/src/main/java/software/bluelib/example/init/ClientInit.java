package software.bluelib.example.init;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexRender;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.EXAMPLE_ONE, DragonRender::new);
        EntityRendererRegistry.register(ModEntities.EXAMPLE_TWO, RexRender::new);
    }
}
