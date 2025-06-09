/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public final class LevelUtils {

    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }
}
