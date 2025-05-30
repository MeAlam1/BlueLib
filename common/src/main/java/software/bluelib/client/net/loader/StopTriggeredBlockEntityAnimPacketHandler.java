package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoBlockEntity;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.net.messages.client.loader.StopTriggeredBlockEntityAnimPacket;

public class StopTriggeredBlockEntityAnimPacketHandler implements ClientNetworkPacketHandler<StopTriggeredBlockEntityAnimPacket> {
	@Override
	public void handle(StopTriggeredBlockEntityAnimPacket pPacket, Minecraft pClient) {
		if (ClientUtil.getLevel().getBlockEntity(pPacket.pos()) instanceof GeoBlockEntity blockEntity)
			blockEntity.stopTriggeredAnim(pPacket.controllerName().isEmpty() ? null : pPacket.controllerName(), pPacket.animName().isEmpty() ? null : pPacket.animName());
	}
}
