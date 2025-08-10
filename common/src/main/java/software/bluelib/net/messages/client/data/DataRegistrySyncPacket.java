/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.data;

import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.NetworkPacket;

@SuppressWarnings({ "unused" })
public abstract class DataRegistrySyncPacket<T, N extends NetworkPacket<N>> implements NetworkPacket<N> {

	@NotNull
	private final Collection<T> registryEntries;

	@Nullable
	public RegistryFriendlyByteBuf buffer;

	@NotNull
	public final ArrayList<T> entries = new ArrayList<>();

	public DataRegistrySyncPacket(@NotNull Collection<T> pRegistryEntries) {
		this.registryEntries = pRegistryEntries;
	}

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		RegistryFriendlyByteBuf newBuffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), pBuffer.registryAccess());
		newBuffer.writeCollection(registryEntries, (buf, entry) -> encodeEntry(newBuffer, entry));
		pBuffer.writeInt(newBuffer.readableBytes());
		pBuffer.writeBytes(newBuffer);
	}

	protected void decodeBuffer(@NotNull RegistryFriendlyByteBuf pBuffer) {
		int size = pBuffer.readInt();
		this.buffer = new RegistryFriendlyByteBuf(pBuffer.readBytes(size), pBuffer.registryAccess());
	}

	public abstract void encodeEntry(@NotNull RegistryFriendlyByteBuf pBuffer, @NotNull T pEntry);

	public abstract T decodeEntry(@NotNull RegistryFriendlyByteBuf pBuffer);

	public abstract void synchronizeDecoded(@NotNull Collection<T> pEntries);
}
