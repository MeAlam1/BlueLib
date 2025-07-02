/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface NetworkManager {

    void sendPacketToPlayer(@NotNull ServerPlayer player, @NotNull NetworkPacket<?> packet);

    void sendToServer(@NotNull NetworkPacket<?> packet);
}
