// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

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
