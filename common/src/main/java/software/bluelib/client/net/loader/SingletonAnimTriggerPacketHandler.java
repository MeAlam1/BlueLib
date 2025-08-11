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
import software.bluelib.api.utils.loader.LoaderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.net.messages.client.loader.SingletonAnimTriggerPacket;

public class SingletonAnimTriggerPacketHandler implements ClientNetworkPacketHandler<SingletonAnimTriggerPacket> {

	@Override
	public void handle(@NotNull SingletonAnimTriggerPacket pPacket, @NotNull Minecraft pClient) {
		BlueAnimatable animatable = LoaderUtils.getSyncedAnimatable(pPacket.syncableId());

		if (animatable != null)
			animatable.getAnimatableInstanceCache().getManagerForId(pPacket.instanceId()).tryTriggerAnimation(pPacket.controllerName(), pPacket.animName());
	}
}
