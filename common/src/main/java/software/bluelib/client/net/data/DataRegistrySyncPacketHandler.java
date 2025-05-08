// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.client.net.data;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.net.messages.client.data.DataRegistrySyncPacket;

public class DataRegistrySyncPacketHandler<P, T extends DataRegistrySyncPacket<P, T>> implements ClientNetworkPacketHandler<T> {

    @Override
    public void handle(T pPacket, Minecraft pClient) {
        RegistryFriendlyByteBuf buffer = pPacket.buffer;
        if (buffer == null) {
            throw new IllegalStateException("Buffer missing on DataRegistrySyncPacket");
        }

        pPacket.entries.clear();
        pPacket.entries.addAll(buffer.readList(buf -> pPacket.decodeEntry((RegistryFriendlyByteBuf) buf)).stream().filter(Objects::nonNull).toList());

        buffer.release();
        pPacket.synchronizeDecoded(pPacket.entries);
    }
}
