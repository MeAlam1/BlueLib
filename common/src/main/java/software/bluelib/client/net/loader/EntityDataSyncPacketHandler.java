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
import software.bluelib.net.messages.client.loader.EntityDataSyncPacket;

public class EntityDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<EntityDataSyncPacket<D>> {

	@Override
	public void handle(@NotNull EntityDataSyncPacket<D> pPacket, @NotNull Minecraft pClient) {
		if (LevelUtils.getLevel() == null) {
			return;
		}
		Entity entity = LevelUtils.getLevel().getEntity(pPacket.entityId());

		if (entity == null)
			return;

		if (!pPacket.isReplacedEntity()) {
			if (entity instanceof BlueEntity BlueEntity)
				BlueEntity.setAnimData(pPacket.dataTicket(), pPacket.data());

			return;
		}

		if (RenderUtils.getReplacedAnimatable(entity.getType()) instanceof BlueReplacedEntity replacedEntity)
			replacedEntity.setAnimData(entity, pPacket.dataTicket(), pPacket.data());
	}
}
