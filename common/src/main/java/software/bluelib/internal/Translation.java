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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;

@ApiStatus.Internal
public class Translation {

	@NotNull
	public static Component translate(@NotNull String pString) {
		return Component.translatable(BlueLibConstants.MOD_ID + "." + pString);
	}

	@NotNull
	public static Component translate(@NotNull String pString, @Nullable Object... pArgs) {
		return Component.translatable(BlueLibConstants.MOD_ID + "." + pString, pArgs);
	}

	@NotNull
	public static Component log(@NotNull String pString) {
		return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString);
	}

	@NotNull
	public static Component log(@NotNull String pString, @Nullable Object... pArgs) {
		return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString, pArgs);
	}

	@NotNull
	public static Component config(@NotNull String pString) {
		return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString);
	}

	@NotNull
	public static Component config(@NotNull String pString, @Nullable Object... pArgs) {
		return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString, pArgs);
	}
}
