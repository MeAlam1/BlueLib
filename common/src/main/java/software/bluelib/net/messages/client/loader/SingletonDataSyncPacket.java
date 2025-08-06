/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.loader;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;

public record SingletonDataSyncPacket<D>(
		@NotNull String syncableId,
		@NotNull Long instanceId,
		@NotNull SerializableDataTicket<D> dataTicket,
		@NotNull D data) implements NetworkPacket<SingletonDataSyncPacket<D>> {

	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("singleton_data_sync");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeUtf(this.syncableId);
		pBuffer.writeVarLong(this.instanceId);
		SerializableDataTicket.STREAM_CODEC.encode(pBuffer, this.dataTicket);
		this.dataTicket.streamCodec().encode(pBuffer, this.data);
	}

	@SuppressWarnings("unchecked")
	public static <D> @NotNull SingletonDataSyncPacket<D> decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		String syncableId = pBuffer.readUtf();
		Long instanceId = pBuffer.readVarLong();
		SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) SerializableDataTicket.STREAM_CODEC.decode(pBuffer);
		D data = dataTicket.streamCodec().decode(pBuffer);
		return new SingletonDataSyncPacket<>(syncableId, instanceId, dataTicket, data);
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}
