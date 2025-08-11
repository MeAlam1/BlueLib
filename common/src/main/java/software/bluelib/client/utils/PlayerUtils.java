/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class PlayerUtils {

	public static @Nullable Player getOptionalClientPlayer() {
		return Minecraft.getInstance().player;
	}

	public static @NotNull Player getClientPlayer() {
		Player player = getOptionalClientPlayer();
		if (player == null) {
			throw new IllegalStateException("Client player is not available, You are probably not in a World or the game is not running.");
		}
		return player;
	}

	public static @Nullable LocalPlayer getOptionalLocalPlayer() {
		return Minecraft.getInstance().player;
	}

	public static @NotNull LocalPlayer getLocalPlayer() {
		LocalPlayer player = getOptionalLocalPlayer();
		if (player == null) {
			throw new IllegalStateException("Local player is not available, You are probably not in a World or the game is not running.");
		}
		return player;
	}

	public static boolean isPlayerInCreativeMode() {
		Player player = getClientPlayer();
		return player.isCreative();
	}

	public static boolean isPlayerSneaking() {
		Player player = getClientPlayer();
		return player.isCrouching();
	}

	public static boolean isPlayerFlying() {
		Player player = getClientPlayer();
		return player.getAbilities().flying;
	}
}
