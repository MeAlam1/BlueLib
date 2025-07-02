/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net.data;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.net.messages.client.data.DataRegistrySyncPacket;

public class DataRegistrySyncPacketHandler<P, T extends DataRegistrySyncPacket<P, T>> implements ClientNetworkPacketHandler<T> {

	@Override
	public void handle(@NotNull T pPacket, @NotNull Minecraft pClient) {
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
