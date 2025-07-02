/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;

public record OpenLoggerPacket() implements NetworkPacket<OpenLoggerPacket> {

	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("open_screen_packet");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {}

	@NotNull
	public static OpenLoggerPacket decode(@NotNull FriendlyByteBuf pBuffer) {
		return new OpenLoggerPacket();
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}
