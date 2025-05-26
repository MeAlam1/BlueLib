/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.packet;

import java.util.function.Consumer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibCommon;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.SingletonGeoAnimatable;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.loader.util.GeckoLibUtil;

public record SingletonDataSyncPacket<D>(String syncableId, long instanceId, SerializableDataTicket<D> dataTicket, D data) implements MultiloaderPacket {

    public static final Type<SingletonDataSyncPacket<?>> TYPE = new Type<>(BlueLibCommon.Resource.resource("singleton_data_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SingletonDataSyncPacket<?>> CODEC = StreamCodec.of((buf, packet) -> {
        SerializableDataTicket.STREAM_CODEC.encode(buf, packet.dataTicket);
        buf.writeUtf(packet.syncableId);
        buf.writeVarLong(packet.instanceId);
        ((StreamCodec) packet.dataTicket.streamCodec()).encode(buf, packet.data);
    }, buf -> {
        final SerializableDataTicket dataTicket = SerializableDataTicket.STREAM_CODEC.decode(buf);

        return new SingletonDataSyncPacket<>(buf.readUtf(), buf.readVarLong(), dataTicket, dataTicket.streamCodec().decode(buf));
    });

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void receiveMessage(@Nullable Player sender, Consumer<Runnable> workQueue) {
        workQueue.accept(() -> {
            GeoAnimatable animatable = GeckoLibUtil.getSyncedAnimatable(this.syncableId);

            if (animatable instanceof SingletonGeoAnimatable singleton)
                singleton.setAnimData(ClientUtil.getClientPlayer(), this.instanceId, this.dataTicket, this.data);
        });
    }
}
