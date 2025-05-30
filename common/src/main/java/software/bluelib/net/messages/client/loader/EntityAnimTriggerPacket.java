package software.bluelib.net.messages.client.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;

public record EntityAnimTriggerPacket(int entityId, boolean isReplacedEntity, String controllerName, String animName) implements NetworkPacket<EntityAnimTriggerPacket> {
	public static final ResourceLocation ID = BlueLibCommon.Resource.resource("entity_anim_trigger");

	@Override
	public void encode(RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeVarInt(this.entityId);
		pBuffer.writeBoolean(this.isReplacedEntity);
		pBuffer.writeUtf(this.controllerName);
		pBuffer.writeUtf(this.animName);
	}

	public static EntityAnimTriggerPacket decode(FriendlyByteBuf pBuffer) {
		int entityId = pBuffer.readVarInt();
		boolean isReplacedEntity = pBuffer.readBoolean();
		String controllerName = pBuffer.readUtf();
		String animName = pBuffer.readUtf();
		return new EntityAnimTriggerPacket(entityId, isReplacedEntity, controllerName, animName);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}