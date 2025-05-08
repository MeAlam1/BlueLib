// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

import net.minecraft.client.Minecraft;

public interface ClientNetworkPacketHandler<T extends NetworkPacket<T>> extends PacketHandler<T> {

    void handle(T pPacket, Minecraft pClient);
}
