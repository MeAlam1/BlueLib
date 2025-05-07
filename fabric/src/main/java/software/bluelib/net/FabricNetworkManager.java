// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.registry.NetworkRegistry;

public class FabricNetworkManager implements BlueLibConstants.NetworkManager {

    public static void registerMessages() {
        NetworkRegistry.s2cPayloads.forEach(info -> FabricPacketInfo.registerPacket(info, true));
        NetworkRegistry.c2sPayloads.forEach(info -> FabricPacketInfo.registerPacket(info, false));
    }

    public static void registerClientHandlers() {
        NetworkRegistry.s2cPayloads.stream()
                .map(FabricPacketInfo::new)
                .forEach(FabricPacketInfo::registerClientHandler);
    }

    public static void registerServerHandlers() {
        NetworkRegistry.c2sPayloads.stream()
                .map(FabricPacketInfo::new)
                .forEach(FabricPacketInfo::registerServerHandler);
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer pPlayer, NetworkPacket<?> pPacket) {
        ServerPlayNetworking.send(pPlayer, pPacket);
    }

    @Override
    public void sendToServer(NetworkPacket<?> pPacket) {
        ClientPlayNetworking.send(pPacket);
    }
}
