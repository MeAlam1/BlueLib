/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;
import software.bluelib.net.serverHandling.TestPacketHandler;

/**
 * {@link TestPacketHandler}
 */
public record TestPacket(@NotNull Boolean value) implements NetworkPacket<TestPacket> {

	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("test_packet");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeBoolean(value);
	}

	@NotNull
	public static TestPacket decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		return new TestPacket(pBuffer.readBoolean());
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}
