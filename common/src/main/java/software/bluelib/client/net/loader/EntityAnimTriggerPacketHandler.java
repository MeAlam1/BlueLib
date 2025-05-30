package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoEntity;
import software.bluelib.loader.animatable.GeoReplacedEntity;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.loader.util.RenderUtil;
import software.bluelib.net.messages.client.loader.EntityAnimTriggerPacket;

public class EntityAnimTriggerPacketHandler implements ClientNetworkPacketHandler<EntityAnimTriggerPacket> {
	@Override
	public void handle(EntityAnimTriggerPacket pPacket, Minecraft pClient) {
		Entity entity = ClientUtil.getLevel().getEntity(pPacket.entityId());

		if (entity == null)
			return;

		String controllerName = pPacket.controllerName().isEmpty() ? null : pPacket.controllerName();
		if (!pPacket.isReplacedEntity()) {
			if (entity instanceof GeoEntity geoEntity)
				geoEntity.triggerAnim(controllerName, pPacket.animName());

			return;
		}

		if (RenderUtil.getReplacedAnimatable(entity.getType()) instanceof GeoReplacedEntity replacedEntity)
			replacedEntity.triggerAnim(entity, controllerName, pPacket.animName());
	}
}
