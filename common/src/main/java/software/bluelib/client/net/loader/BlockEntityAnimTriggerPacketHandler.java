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
import software.bluelib.client.utils.LevelUtils;
import software.bluelib.loader.animatable.block.BlueBlockEntity;
import software.bluelib.net.messages.client.loader.BlockEntityAnimTriggerPacket;

public class BlockEntityAnimTriggerPacketHandler implements ClientNetworkPacketHandler<BlockEntityAnimTriggerPacket> {

	@Override
	public void handle(@NotNull BlockEntityAnimTriggerPacket pPacket, @NotNull Minecraft pClient) {
		if (LevelUtils.getLevel() == null) {
			return;
		}
		if (LevelUtils.getLevel().getBlockEntity(pPacket.pos()) instanceof BlueBlockEntity blockEntity)
			blockEntity.triggerAnim(pPacket.controllerName().isEmpty() ? null : pPacket.controllerName(), pPacket.animName());
	}
}
