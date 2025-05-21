/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.minecraft;

import net.minecraft.client.Minecraft;

public class ClientUtils {

    public static boolean isInWorld() {
        return Minecraft.getInstance().level != null;
    }
}
