/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.math;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MoLangMathUtils {

	@NotNull
	public static Double toDouble(@NotNull Object pObj) {
		if (pObj instanceof Number) return ((Number) pObj).doubleValue();
		try {
			return Double.parseDouble(pObj.toString());
		} catch (Exception e) {
			return 0.0;
		}
	}

	@NotNull
	public static Double toDouble(@NotNull List<Object> pArgs, @NotNull Integer pIndex) {
		return toDouble(pArgs, pIndex, 0.0);
	}

	@NotNull
	public static Double toDouble(@NotNull List<Object> pArgs, @NotNull Integer pIndex, @NotNull Double pFallback) {
		if (pIndex >= pArgs.size()) return pFallback;
		return toDouble(pArgs.get(pIndex));
	}
}
