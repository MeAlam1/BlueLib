// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface ServerNetworkPacketHandler<T extends NetworkPacket<T>> extends PacketHandler<T> {

    void handle(T pPacket, MinecraftServer pServer, ServerPlayer pPlayer);
}
