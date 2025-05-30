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
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoEntity;
import software.bluelib.loader.animatable.GeoReplacedEntity;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.loader.util.RenderUtil;
import software.bluelib.net.messages.client.loader.EntityDataSyncPacket;

public class EntityDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<EntityDataSyncPacket<D>> {

    @Override
    public void handle(EntityDataSyncPacket<D> pPacket, Minecraft pClient) {
        Entity entity = ClientUtil.getLevel().getEntity(pPacket.entityId());

        if (entity == null)
            return;

        if (!pPacket.isReplacedEntity()) {
            if (entity instanceof GeoEntity geoEntity)
                geoEntity.setAnimData(pPacket.dataTicket(), pPacket.data());

            return;
        }

        if (RenderUtil.getReplacedAnimatable(entity.getType()) instanceof GeoReplacedEntity replacedEntity)
            replacedEntity.setAnimData(entity, pPacket.dataTicket(), pPacket.data());
    }
}
