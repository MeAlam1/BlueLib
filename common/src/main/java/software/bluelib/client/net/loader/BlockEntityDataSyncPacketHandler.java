package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoBlockEntity;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.net.messages.client.loader.BlockEntityDataSyncPacket;

public class BlockEntityDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<BlockEntityDataSyncPacket<D>> {
	@Override
	public void handle(BlockEntityDataSyncPacket<D> pPacket, Minecraft pClient) {
		if (ClientUtil.getLevel().getBlockEntity(pPacket.pos()) instanceof GeoBlockEntity blockEntity) {
			blockEntity.setAnimData(pPacket.dataTicket(), pPacket.data());
		}
	}
}
