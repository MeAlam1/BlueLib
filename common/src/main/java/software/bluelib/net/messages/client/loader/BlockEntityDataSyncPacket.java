/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;

public record BlockEntityDataSyncPacket<D>(
		@NotNull BlockPos pos,
		@NotNull SerializableDataTicket<D> dataTicket,
		@NotNull D data) implements NetworkPacket<BlockEntityDataSyncPacket<D>> {

	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("blockentity_data_sync");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		SerializableDataTicket.STREAM_CODEC.encode(pBuffer, dataTicket);
		pBuffer.writeBlockPos(pos);
		dataTicket.streamCodec().encode(pBuffer, data);
	}

	@SuppressWarnings("unchecked")
	public static <D> @NotNull BlockEntityDataSyncPacket<D> decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) SerializableDataTicket.STREAM_CODEC.decode(pBuffer);
		BlockPos pos = pBuffer.readBlockPos();
		D data = dataTicket.streamCodec().decode(pBuffer);
		return new BlockEntityDataSyncPacket<>(pos, dataTicket, data);
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}
