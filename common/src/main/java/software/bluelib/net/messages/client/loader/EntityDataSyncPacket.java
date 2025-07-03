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
import software.bluelib.oldLoader.constant.dataticket.SerializableDataTicket;

public record EntityDataSyncPacket<D>(
		@NotNull Integer entityId,
		boolean isReplacedEntity,
		@NotNull SerializableDataTicket<D> dataTicket,
		@NotNull D data) implements NetworkPacket<EntityDataSyncPacket<D>> {

	public static final ResourceLocation ID = BlueResource.resource("entity_data_sync");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		SerializableDataTicket.STREAM_CODEC.encode(pBuffer, this.dataTicket);
		pBuffer.writeVarInt(this.entityId);
		pBuffer.writeBoolean(this.isReplacedEntity);
		this.dataTicket.streamCodec().encode(pBuffer, this.data);
	}

	@SuppressWarnings("unchecked")
	public static <D> @NotNull EntityDataSyncPacket<D> decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) SerializableDataTicket.STREAM_CODEC.decode(pBuffer);
		Integer entityId = pBuffer.readVarInt();
		boolean isReplacedEntity = pBuffer.readBoolean();
		D data = dataTicket.streamCodec().decode(pBuffer);
		return new EntityDataSyncPacket<>(entityId, isReplacedEntity, dataTicket, data);
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}
