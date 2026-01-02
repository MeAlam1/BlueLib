/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.PacketProvider;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.client.loader.BlockEntityAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.BlockEntityDataSyncPacket;
import software.bluelib.net.messages.client.loader.EntityAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.EntityDataSyncPacket;
import software.bluelib.net.messages.client.loader.SingletonAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.SingletonDataSyncPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredBlockEntityAnimPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredEntityAnimPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredSingletonAnimPacket;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

@ApiStatus.Internal
public class BlueNetworkRegistry implements PacketProvider {

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getC2SPackets() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, TestPacketHandler::new));

		return list;
	}

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getS2CPackets() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode));

		list.add(new PacketRegisterInfo<>(BlockEntityAnimTriggerPacket.ID, BlockEntityAnimTriggerPacket::decode));
		list.add(new PacketRegisterInfo<>(BlockEntityDataSyncPacket.ID, BlockEntityDataSyncPacket::decode));
		list.add(new PacketRegisterInfo<>(EntityAnimTriggerPacket.ID, EntityAnimTriggerPacket::decode));
		list.add(new PacketRegisterInfo<>(EntityDataSyncPacket.ID, EntityDataSyncPacket::decode));
		list.add(new PacketRegisterInfo<>(SingletonAnimTriggerPacket.ID, SingletonAnimTriggerPacket::decode));
		list.add(new PacketRegisterInfo<>(SingletonDataSyncPacket.ID, SingletonDataSyncPacket::decode));
		list.add(new PacketRegisterInfo<>(StopTriggeredEntityAnimPacket.ID, StopTriggeredEntityAnimPacket::decode));
		list.add(new PacketRegisterInfo<>(StopTriggeredBlockEntityAnimPacket.ID, StopTriggeredBlockEntityAnimPacket::decode));
		list.add(new PacketRegisterInfo<>(StopTriggeredSingletonAnimPacket.ID, StopTriggeredSingletonAnimPacket::decode));

		return list;
	}
}
