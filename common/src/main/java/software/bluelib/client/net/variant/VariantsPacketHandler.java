/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net.variant;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.net.messages.client.variant.VariantsPacket;

@SuppressWarnings({ "unused" })
public record VariantsPacketHandler(
		Consumer<VariantsPacket> handlerFunction) implements ClientNetworkPacketHandler<VariantsPacket> {

	public VariantsPacketHandler(@NotNull Consumer<VariantsPacket> handlerFunction) {
		this.handlerFunction = handlerFunction;
	}

	@Override
	public void handle(@NotNull VariantsPacket pPacket, @NotNull Minecraft pClient) {
		handlerFunction.accept(pPacket);
	}
}
