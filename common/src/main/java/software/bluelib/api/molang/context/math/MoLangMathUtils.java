/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.math;

import java.util.List;

public class MoLangMathUtils {

	public static double toDouble(Object pObj) {
		if (pObj instanceof Number) return ((Number) pObj).doubleValue();
		try {
			return Double.parseDouble(pObj.toString());
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static double toDouble(List<Object> pArgs, int pIndex) {
		return toDouble(pArgs, pIndex, 0.0);
	}

	public static double toDouble(List<Object> pArgs, int pIndex, double pFallback) {
		if (pArgs == null || pIndex >= pArgs.size()) return pFallback;
		return toDouble(pArgs.get(pIndex));
	}
}
