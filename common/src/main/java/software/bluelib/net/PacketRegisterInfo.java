/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import java.util.function.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.Encodable;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.PacketHandler;

public class PacketRegisterInfo<T extends NetworkPacket<T> & Encodable> {

    private final ResourceLocation id;
    private final Function<RegistryFriendlyByteBuf, T> decoder;
    private final PacketHandler<T> handler;
    private final CustomPacketPayload.Type<T> payloadId;
    private final StreamCodec<RegistryFriendlyByteBuf, T> codec;

    public PacketRegisterInfo(ResourceLocation pId,
            Function<RegistryFriendlyByteBuf, T> pDecoder,
            PacketHandler<T> pHandler,
            StreamCodec<RegistryFriendlyByteBuf, T> pCodec) {
        this.id = pId;
        this.decoder = pDecoder;
        this.handler = pHandler;
        this.payloadId = new CustomPacketPayload.Type<>(pId);
        this.codec = pCodec != null ? pCodec : createDefaultCodec(pDecoder);
    }

    public PacketRegisterInfo(ResourceLocation pId,
            Function<RegistryFriendlyByteBuf, T> pDecoder,
            PacketHandler<T> pHandler) {
        this.id = pId;
        this.decoder = pDecoder;
        this.handler = pHandler;
        this.payloadId = new CustomPacketPayload.Type<>(pId);
        this.codec = createDefaultCodec(pDecoder);
    }

    private StreamCodec<RegistryFriendlyByteBuf, T> createDefaultCodec(Function<RegistryFriendlyByteBuf, T> pDecoder) {
        return StreamCodec.of(
                (buf, packet) -> packet.encode(buf),
                pDecoder::apply);
    }

    public ResourceLocation getId() {
        return id;
    }

    public Function<RegistryFriendlyByteBuf, T> getDecoder() {
        return decoder;
    }

    public PacketHandler<T> getHandler() {
        return handler;
    }

    public CustomPacketPayload.Type<T> getPayloadId() {
        return payloadId;
    }

    public StreamCodec<RegistryFriendlyByteBuf, T> getCodec() {
        return codec;
    }
}
