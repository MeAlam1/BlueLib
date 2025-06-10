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
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animatable.SingletonBlueAnimatable;
import software.bluelib.net.messages.client.loader.SingletonDataSyncPacket;

public class SingletonDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<SingletonDataSyncPacket<D>> {

    @Override
    public void handle(SingletonDataSyncPacket<D> pPacket, Minecraft pClient) {
        BlueAnimatable animatable = LoaderUtils.getSyncedAnimatable(pPacket.syncableId());

        if (animatable instanceof SingletonBlueAnimatable singleton)
            singleton.setAnimData(PlayerUtils.getClientPlayer(), pPacket.instanceId(), pPacket.dataTicket(), pPacket.data());
    }
}
