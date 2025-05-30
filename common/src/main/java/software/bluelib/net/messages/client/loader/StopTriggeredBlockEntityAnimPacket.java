package software.bluelib.net.messages.client.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;

public record StopTriggeredBlockEntityAnimPacket(BlockPos pos, String controllerName, String animName) implements NetworkPacket<StopTriggeredBlockEntityAnimPacket> {
	public static final ResourceLocation ID = BlueLibCommon.Resource.resource("stop_triggered_blockentity_anim");

	@Override
	public void encode(RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeBlockPos(pos);
		pBuffer.writeUtf(controllerName);
		pBuffer.writeUtf(animName);
	}

	public static StopTriggeredBlockEntityAnimPacket decode(FriendlyByteBuf pBuffer) {
		BlockPos pos = pBuffer.readBlockPos();
		String controllerName = pBuffer.readUtf();
		String animName = pBuffer.readUtf();
		return new StopTriggeredBlockEntityAnimPacket(pos, controllerName, animName);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}