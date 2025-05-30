package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.util.GeckoLibUtil;
import software.bluelib.net.messages.client.loader.SingletonAnimTriggerPacket;

public class SingletonAnimTriggerPacketHandler implements ClientNetworkPacketHandler<SingletonAnimTriggerPacket> {
	@Override
	public void handle(SingletonAnimTriggerPacket pPacket, Minecraft pClient) {
		GeoAnimatable animatable = GeckoLibUtil.getSyncedAnimatable(pPacket.syncableId());

		if (animatable != null)
			animatable.getAnimatableInstanceCache().getManagerForId(pPacket.instanceId()).tryTriggerAnimation(pPacket.controllerName(), pPacket.animName());
	}
}
