/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net.loader;

import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.loader.animatable.GeoBlockEntity;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.net.messages.client.loader.BlockEntityAnimTriggerPacket;

public class BlockEntityAnimTriggerPacketHandler implements ClientNetworkPacketHandler<BlockEntityAnimTriggerPacket> {

    @Override
    public void handle(BlockEntityAnimTriggerPacket pPacket, Minecraft pClient) {
        if (ClientUtil.getLevel().getBlockEntity(pPacket.pos()) instanceof GeoBlockEntity blockEntity)
            blockEntity.triggerAnim(pPacket.controllerName().isEmpty() ? null : pPacket.controllerName(), pPacket.animName());
    }
}
