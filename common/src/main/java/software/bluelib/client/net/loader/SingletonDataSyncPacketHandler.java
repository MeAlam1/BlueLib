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
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.base.SingletonBlueAnimatable;
import software.bluelib.net.messages.client.loader.SingletonDataSyncPacket;

public class SingletonDataSyncPacketHandler<D> implements ClientNetworkPacketHandler<SingletonDataSyncPacket<D>> {

	@Override
	public void handle(@NotNull SingletonDataSyncPacket<D> pPacket, @NotNull Minecraft pClient) {
		BlueAnimatable animatable = LoaderUtils.getSyncedAnimatable(pPacket.syncableId());

		if (animatable instanceof SingletonBlueAnimatable singleton)
			singleton.setAnimData(PlayerUtils.getClientPlayer(), pPacket.instanceId(), pPacket.dataTicket(), pPacket.data());
	}
}
