/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.internal.registry;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.PacketProvider;
import software.bluelib.client.net.OpenLoggerPacketHandler;
import software.bluelib.client.net.loader.*;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.client.loader.*;
import software.bluelib.net.messages.server.TestPacket;

@ApiStatus.Internal
public class BlueClientNetworkRegistry implements PacketProvider {

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getC2SPackets() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode));

		return list;
	}

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getS2CPackets() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		// Logger
		list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode, OpenLoggerPacketHandler::new));

		// Loader
		list.add(new PacketRegisterInfo<>(BlockEntityAnimTriggerPacket.ID, BlockEntityAnimTriggerPacket::decode, BlockEntityAnimTriggerPacketHandler::new));
		list.add(new PacketRegisterInfo<>(BlockEntityDataSyncPacket.ID, BlockEntityDataSyncPacket::decode, BlockEntityDataSyncPacketHandler::new));
		list.add(new PacketRegisterInfo<>(EntityAnimTriggerPacket.ID, EntityAnimTriggerPacket::decode, EntityAnimTriggerPacketHandler::new));
		list.add(new PacketRegisterInfo<>(EntityDataSyncPacket.ID, EntityDataSyncPacket::decode, EntityDataSyncPacketHandler::new));
		list.add(new PacketRegisterInfo<>(SingletonAnimTriggerPacket.ID, SingletonAnimTriggerPacket::decode, SingletonAnimTriggerPacketHandler::new));
		list.add(new PacketRegisterInfo<>(SingletonDataSyncPacket.ID, SingletonDataSyncPacket::decode, SingletonDataSyncPacketHandler::new));
		list.add(new PacketRegisterInfo<>(StopTriggeredEntityAnimPacket.ID, StopTriggeredEntityAnimPacket::decode, StopTriggeredEntityAnimPacketHandler::new));
		list.add(new PacketRegisterInfo<>(StopTriggeredBlockEntityAnimPacket.ID, StopTriggeredBlockEntityAnimPacket::decode, StopTriggeredBlockEntityAnimPacketHandler::new));
		list.add(new PacketRegisterInfo<>(StopTriggeredSingletonAnimPacket.ID, StopTriggeredSingletonAnimPacket::decode, StopTriggeredSingletonAnimPacketHandler::new));

		return list;
	}
}
