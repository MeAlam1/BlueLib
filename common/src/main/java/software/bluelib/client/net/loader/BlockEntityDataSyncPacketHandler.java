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
import software.bluelib.client.utils.LevelUtils;
import software.bluelib.loader.animatable.BlueBlockEntity;
import software.bluelib.net.messages.client.loader.BlockEntityDataSyncPacket;

public class BlockEntityDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<BlockEntityDataSyncPacket<D>> {

    @Override
    public void handle(BlockEntityDataSyncPacket<D> pPacket, Minecraft pClient) {
        if (LevelUtils.getLevel().getBlockEntity(pPacket.pos()) instanceof BlueBlockEntity blockEntity) {
            blockEntity.setAnimData(pPacket.dataTicket(), pPacket.data());
        }
    }
}
