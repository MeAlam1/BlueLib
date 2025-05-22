/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;

public class FabricPacketInfo<T extends NetworkPacket<T>> {

    private final PacketRegisterInfo<T> info;

    public FabricPacketInfo(PacketRegisterInfo<T> pInfo) {
        this.info = pInfo;
    }

    public static <T extends NetworkPacket<T>> void registerPacket(PacketRegisterInfo<T> pInfo, boolean pClient) {
        PayloadTypeRegistry<RegistryFriendlyByteBuf> registry = pClient ? PayloadTypeRegistry.playS2C() : PayloadTypeRegistry.playC2S();
        registry.register(pInfo.getPayloadId(), pInfo.getCodec());
    }

    public void registerClientHandler() {
        ClientPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, ignored) -> {
            ClientNetworkPacketHandler<T> handler = (ClientNetworkPacketHandler<T>) info.getHandler();
            handler.handle(obj, Minecraft.getInstance());
        });
    }

    public void registerServerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, context) -> {
            ServerNetworkPacketHandler<T> handler = (ServerNetworkPacketHandler<T>) info.getHandler();
            handler.handle(obj, context.player().getServer(), context.player());
        });
    }
}
