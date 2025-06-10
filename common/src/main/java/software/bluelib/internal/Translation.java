/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.BlueLibConstants;

@ApiStatus.Internal
public class Translation {

    public static Component translate(String pString) {
        return Component.translatable(BlueLibConstants.MOD_ID + "." + pString);
    }

    public static Component translate(String pString, Object... pArgs) {
        return Component.translatable(BlueLibConstants.MOD_ID + "." + pString, pArgs);
    }

    public static Component log(String pString) {
        return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString);
    }

    public static Component log(String pString, Object... pArgs) {
        return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString, pArgs);
    }

    public static Component config(String pString) {
        return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString);
    }

    public static Component config(String pString, Object... pArgs) {
        return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString, pArgs);
    }
}
