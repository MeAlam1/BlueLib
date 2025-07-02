/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import software.bluelib.net.PacketRegisterInfo;

public interface PacketProvider {

	interface C2SPacketProvider {

		@NotNull
		List<PacketRegisterInfo<?>> getC2SPacketInfoList();
	}

	interface S2CPacketProvider {

		@NotNull
		List<PacketRegisterInfo<?>> getS2CPacketInfoList();
	}
}
