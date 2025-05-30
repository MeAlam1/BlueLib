package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.util.GeckoLibUtil;
import software.bluelib.net.messages.client.loader.StopTriggeredSingletonAnimPacket;

public class StopTriggeredSingletonAnimPacketHandler implements ClientNetworkPacketHandler<StopTriggeredSingletonAnimPacket> {
	@Override
	public void handle(StopTriggeredSingletonAnimPacket pPacket, Minecraft pClient) {
		GeoAnimatable animatable = GeckoLibUtil.getSyncedAnimatable(pPacket.syncableId());

		if (animatable != null) {
			AnimatableManager<GeoAnimatable> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(pPacket.instanceId());

			if (animatableManager != null)
				animatableManager.stopTriggeredAnimation(pPacket.controllerName().isEmpty() ? null : pPacket.controllerName(), pPacket.animName().isEmpty() ? null : pPacket.animName());
		}
	}
}
