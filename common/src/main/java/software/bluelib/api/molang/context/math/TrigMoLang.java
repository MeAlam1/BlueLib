/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.math;

import software.bluelib.api.molang.MoLangNamespaceUtils;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class TrigMoLang extends BaseMoLangContext {

	public TrigMoLang() {
		registerFunction(MoLangNamespaceUtils.withMathNamespace("sin"), (args, runtime) -> Math.sin(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("cos"), (args, runtime) -> Math.cos(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("tan"), (args, runtime) -> Math.tan(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("asin"), (args, runtime) -> Math.asin(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("acos"), (args, runtime) -> Math.acos(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("atan"), (args, runtime) -> Math.atan(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("atan2"), (args, runtime) -> Math.atan2(MoLangMathUtils.toDouble(args, 0), MoLangMathUtils.toDouble(args, 1)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("to_deg"), (args, runtime) -> Math.toDegrees(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("to_rad"), (args, runtime) -> Math.toRadians(MoLangMathUtils.toDouble(args, 0)));
	}
}
