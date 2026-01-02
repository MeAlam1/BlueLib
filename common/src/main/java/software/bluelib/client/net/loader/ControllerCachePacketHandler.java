/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.net.messages.client.loader.ControllerCachePacket;

public class ControllerCachePacketHandler implements ClientNetworkPacketHandler<ControllerCachePacket> {

	@Override
	public void handle(@NotNull ControllerCachePacket pPacket, @NotNull Minecraft pClient) {
		pClient.execute(() -> ResourceCache.Client.setControllers(pPacket.allControllers()));
	}
}
