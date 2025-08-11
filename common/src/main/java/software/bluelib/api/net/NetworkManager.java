/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface NetworkManager {

	void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket);

	void sendToServer(@NotNull NetworkPacket<?> pPacket);

	void sendToAllPlayersTrackingEntity(@NotNull Entity pTrackingEntity, @NotNull NetworkPacket<?> pPacket);

	void sendToAllPlayersTrackingBlock(@NotNull ServerLevel pLevel, @NotNull BlockPos pBlockPos, @NotNull NetworkPacket<?> pPacket);
}
