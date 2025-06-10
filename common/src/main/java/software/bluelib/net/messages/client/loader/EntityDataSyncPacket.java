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
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;

public record EntityDataSyncPacket<D>(int entityId, boolean isReplacedEntity, SerializableDataTicket<D> dataTicket,
        D data) implements NetworkPacket<EntityDataSyncPacket<D>> {

    public static final ResourceLocation ID = BlueResource.resource("entity_data_sync");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        SerializableDataTicket.STREAM_CODEC.encode(pBuffer, this.dataTicket);
        pBuffer.writeVarInt(this.entityId);
        pBuffer.writeBoolean(this.isReplacedEntity);
        this.dataTicket.streamCodec().encode(pBuffer, this.data);
    }

    @SuppressWarnings("unchecked")
    public static <D> EntityDataSyncPacket<D> decode(RegistryFriendlyByteBuf pBuffer) {
        SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) SerializableDataTicket.STREAM_CODEC.decode(pBuffer);
        int entityId = pBuffer.readVarInt();
        boolean isReplacedEntity = pBuffer.readBoolean();
        D data = dataTicket.streamCodec().decode(pBuffer);
        return new EntityDataSyncPacket<>(entityId, isReplacedEntity, dataTicket, data);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
