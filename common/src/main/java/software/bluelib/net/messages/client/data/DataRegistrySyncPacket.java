// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.net.messages.client.data;

import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import software.bluelib.api.net.NetworkPacket;

public abstract class DataRegistrySyncPacket<T, N extends NetworkPacket<N>> implements NetworkPacket<N> {

    private final Collection<T> registryEntries;
    public RegistryFriendlyByteBuf buffer;
    public final ArrayList<T> entries = new ArrayList<>();

    public DataRegistrySyncPacket(Collection<T> pRegistryEntries) {
        this.registryEntries = pRegistryEntries;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        RegistryFriendlyByteBuf newBuffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), pBuffer.registryAccess());
        newBuffer.writeCollection(registryEntries, (buf, entry) -> encodeEntry(newBuffer, entry));
        pBuffer.writeInt(newBuffer.readableBytes());
        pBuffer.writeBytes(newBuffer);
    }

    protected void decodeBuffer(RegistryFriendlyByteBuf pBuffer) {
        int size = pBuffer.readInt();
        this.buffer = new RegistryFriendlyByteBuf(pBuffer.readBytes(size), pBuffer.registryAccess());
    }

    public abstract void encodeEntry(RegistryFriendlyByteBuf pBuffer, T pEntry);

    public abstract T decodeEntry(RegistryFriendlyByteBuf pBuffer);

    public abstract void synchronizeDecoded(Collection<T> pEntries);
}
