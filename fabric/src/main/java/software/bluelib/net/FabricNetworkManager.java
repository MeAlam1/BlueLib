/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.NetworkRegistry;

public class FabricNetworkManager implements NetworkManager {

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
    public void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
        ServerPlayNetworking.send(pPlayer, pPacket);
    }

    @Override
    public void sendToServer(@NotNull NetworkPacket<?> pPacket) {
        ClientPlayNetworking.send(pPacket);
    }
}
