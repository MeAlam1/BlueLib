package software.bluelib.net.messages.client.loader;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;

public record SingletonDataSyncPacket<D>(String syncableId, long instanceId, SerializableDataTicket<D> dataTicket,
                                         D data) implements NetworkPacket<SingletonDataSyncPacket<D>> {
	public static final ResourceLocation ID = BlueLibCommon.Resource.resource("singleton_data_sync");

	@Override
	public void encode(RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeUtf(this.syncableId);
		pBuffer.writeVarLong(this.instanceId);
		SerializableDataTicket.STREAM_CODEC.encode(pBuffer, this.dataTicket);
		this.dataTicket.streamCodec().encode(pBuffer, this.data);
	}

	@SuppressWarnings("unchecked")
	public static <D> SingletonDataSyncPacket<D> decode(RegistryFriendlyByteBuf pBuffer) {
		String syncableId = pBuffer.readUtf();
		long instanceId = pBuffer.readVarLong();
		SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) SerializableDataTicket.STREAM_CODEC.decode(pBuffer);
		D data = dataTicket.streamCodec().decode(pBuffer);
		return new SingletonDataSyncPacket<>(syncableId, instanceId, dataTicket, data);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}