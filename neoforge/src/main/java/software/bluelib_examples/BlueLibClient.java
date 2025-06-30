
package software.bluelib_examples;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import software.bluelib_examples.client.BlueLibCommonClient;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BlueLibClient {

    public static void init() {
        BlueLibCommon.doClientRegistration();
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        BlueLibCommonClient.registerRenderers(event::registerEntityRenderer, event::registerBlockEntityRenderer);
    }
}
