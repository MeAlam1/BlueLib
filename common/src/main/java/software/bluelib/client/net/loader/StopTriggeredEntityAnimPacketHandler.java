/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.client.utils.LevelUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.entity.BlueEntity;
import software.bluelib.loader.animatable.entity.BlueReplacedEntity;
import software.bluelib.net.messages.client.loader.StopTriggeredEntityAnimPacket;

public class StopTriggeredEntityAnimPacketHandler implements ClientNetworkPacketHandler<StopTriggeredEntityAnimPacket> {

	@Override
	public void handle(@NotNull StopTriggeredEntityAnimPacket pPacket, @NotNull Minecraft pClient) {
		Entity entity = LevelUtils.getLevel().getEntity(pPacket.entityId());

		if (entity == null)
			return;

		String controllerName = pPacket.controllerName().isEmpty() ? null : pPacket.controllerName();
		String animName = pPacket.animName().isEmpty() ? null : pPacket.animName();
		if (!pPacket.isReplacedEntity()) {
			if (entity instanceof BlueEntity BlueEntity)
				BlueEntity.stopTriggeredAnim(controllerName, animName);

			return;
		}

		if (RenderUtils.getReplacedAnimatable(entity.getType()) instanceof BlueReplacedEntity replacedEntity)
			replacedEntity.stopTriggeredAnim(entity, controllerName, animName);
	}
}
