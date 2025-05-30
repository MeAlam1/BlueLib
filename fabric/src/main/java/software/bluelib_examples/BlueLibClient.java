package software.bluelib_examples;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import software.bluelib_examples.client.BlueLibCommonClient;

public class BlueLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlueLibCommonClient.registerRenderers(EntityRendererRegistry::register, BlockEntityRenderers::register);
        BlueLibCommon.doClientRegistration();
    }
}
