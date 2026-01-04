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

	@NotNull
	default List<PacketRegisterInfo<?>> getC2SPackets() {
		return List.of();
	}

	;

	@NotNull
	default List<PacketRegisterInfo<?>> getS2CPackets() {
		return List.of();
	}

	;
}
