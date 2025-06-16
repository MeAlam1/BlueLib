package software.bluelib.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.loader.cache.ResourceCache;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void reloadClient(AddReloadListenerEvent pEvent) {
		ResourceCache.registerReloadListener();
	}
}
