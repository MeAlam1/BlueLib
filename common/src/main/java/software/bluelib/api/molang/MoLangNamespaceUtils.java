/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import org.jetbrains.annotations.NotNull;

public class MoLangNamespaceUtils {

	@NotNull
	public static String withMathNamespace(@NotNull String pName) {
		return "math." + pName;
	}
}
