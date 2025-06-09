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

public class PlayerUtils {

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static LocalPlayer getLocalPlayer() {
        return Minecraft.getInstance().player instanceof LocalPlayer ? (LocalPlayer) Minecraft.getInstance().player : null;
    }

    public static boolean isPlayerInCreativeMode() {
        Player player = getClientPlayer();
        return player != null && player.isCreative();
    }

    public static boolean isPlayerSneaking() {
        Player player = getClientPlayer();
        return player != null && player.isCrouching();
    }

    public static boolean isPlayerFlying() {
        Player player = getClientPlayer();
        return player != null && player.getAbilities().flying;
    }
}
