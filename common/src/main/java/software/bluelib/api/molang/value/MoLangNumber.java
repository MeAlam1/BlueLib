/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.value;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.MoLangRuntime;

public record MoLangNumber(@NotNull Float value) implements MoLangValue {

	@Override
	public @NotNull Object evaluate(@NotNull MoLangRuntime pRuntime) {
		return value;
	}
}
