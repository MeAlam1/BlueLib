package software.bluelib.net.messages.client.loader;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;

public record SingletonAnimTriggerPacket(String syncableId, long instanceId, String controllerName,
                                         String animName) implements NetworkPacket<SingletonAnimTriggerPacket> {
	public static final ResourceLocation ID = BlueLibCommon.Resource.resource("singleton_anim_trigger");

	@Override
	public void encode(RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeUtf(this.syncableId);
		pBuffer.writeVarLong(this.instanceId);
		pBuffer.writeUtf(this.controllerName);
		pBuffer.writeUtf(this.animName);
	}

	public static SingletonAnimTriggerPacket decode(FriendlyByteBuf pBuffer) {
		String syncableId = pBuffer.readUtf();
		long instanceId = pBuffer.readVarLong();
		String controllerName = pBuffer.readUtf();
		String animName = pBuffer.readUtf();
		return new SingletonAnimTriggerPacket(syncableId, instanceId, controllerName, animName);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}