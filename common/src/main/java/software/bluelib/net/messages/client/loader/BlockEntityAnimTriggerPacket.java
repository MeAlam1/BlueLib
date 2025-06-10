/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;

public record BlockEntityAnimTriggerPacket(BlockPos pos, String controllerName,
                                           String animName) implements NetworkPacket<BlockEntityAnimTriggerPacket> {

	public static final ResourceLocation ID = BlueResource.resource("blockentity_anim_trigger");

	@Override
	public void encode(RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeBlockPos(pos);
		pBuffer.writeUtf(controllerName);
		pBuffer.writeUtf(animName);
	}

	public static BlockEntityAnimTriggerPacket decode(FriendlyByteBuf pBuffer) {
		BlockPos pos = pBuffer.readBlockPos();
		String controllerName = pBuffer.readUtf();
		String animName = pBuffer.readUtf();
		return new BlockEntityAnimTriggerPacket(pos, controllerName, animName);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}
