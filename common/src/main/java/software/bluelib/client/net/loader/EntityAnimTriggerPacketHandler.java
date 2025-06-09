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
import software.bluelib.client.utils.LevelUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.GeoEntity;
import software.bluelib.loader.animatable.GeoReplacedEntity;
import software.bluelib.net.messages.client.loader.EntityAnimTriggerPacket;

public class EntityAnimTriggerPacketHandler implements ClientNetworkPacketHandler<EntityAnimTriggerPacket> {

    @Override
    public void handle(EntityAnimTriggerPacket pPacket, Minecraft pClient) {
        Entity entity = LevelUtils.getLevel().getEntity(pPacket.entityId());

        if (entity == null)
            return;

        String controllerName = pPacket.controllerName().isEmpty() ? null : pPacket.controllerName();
        if (!pPacket.isReplacedEntity()) {
            if (entity instanceof GeoEntity geoEntity)
                geoEntity.triggerAnim(controllerName, pPacket.animName());

            return;
        }

        if (RenderUtils.getReplacedAnimatable(entity.getType()) instanceof GeoReplacedEntity replacedEntity)
            replacedEntity.triggerAnim(entity, controllerName, pPacket.animName());
    }
}
