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
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.net.messages.client.loader.StopTriggeredSingletonAnimPacket;
import software.bluelib.oldLoader.animation.AnimatableManager;

public class StopTriggeredSingletonAnimPacketHandler implements ClientNetworkPacketHandler<StopTriggeredSingletonAnimPacket> {

	@Override
	public void handle(@NotNull StopTriggeredSingletonAnimPacket pPacket, @NotNull Minecraft pClient) {
		BlueAnimatable animatable = LoaderUtils.getSyncedAnimatable(pPacket.syncableId());

		if (animatable != null) {
			AnimatableManager<BlueAnimatable> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(pPacket.instanceId());

			if (animatableManager != null)
				animatableManager.stopTriggeredAnimation(pPacket.controllerName().isEmpty() ? null : pPacket.controllerName(), pPacket.animName().isEmpty() ? null : pPacket.animName());
		}
	}
}
