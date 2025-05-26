/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface ServerNetworkPacketHandler<T extends NetworkPacket<T>> extends PacketHandler<T> {

    void handle(T pPacket, MinecraftServer pServer, ServerPlayer pPlayer);
}
