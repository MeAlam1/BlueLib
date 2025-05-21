

package software.bluelib_examples;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import software.bluelib_examples.client.BlueLibCommonClient;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT)
public class BlueLibClient {

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		BlueLibCommonClient.registerRenderers(event::registerEntityRenderer, event::registerBlockEntityRenderer);
	}
}
