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
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.SingletonGeoAnimatable;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.loader.util.GeckoLibUtil;
import software.bluelib.net.messages.client.loader.SingletonDataSyncPacket;

public class SingletonDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<SingletonDataSyncPacket<D>> {

    @Override
    public void handle(SingletonDataSyncPacket<D> pPacket, Minecraft pClient) {
        GeoAnimatable animatable = GeckoLibUtil.getSyncedAnimatable(pPacket.syncableId());

        if (animatable instanceof SingletonGeoAnimatable singleton)
            singleton.setAnimData(ClientUtil.getClientPlayer(), pPacket.instanceId(), pPacket.dataTicket(), pPacket.data());
    }
}
