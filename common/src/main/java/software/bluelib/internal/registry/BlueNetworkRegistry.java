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
import software.bluelib.client.net.OpenLoggerPacketHandler;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

@ApiStatus.Internal
public class BlueNetworkRegistry implements PacketProvider.C2SPacketProvider, PacketProvider.S2CPacketProvider {

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getC2SPacketInfoList() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		// Test
		list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler()));

		return list;
	}

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getS2CPacketInfoList() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		// Logger
		list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode, new OpenLoggerPacketHandler()));

		return list;
	}
}
