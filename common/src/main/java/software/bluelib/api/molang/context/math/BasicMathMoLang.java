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

public class BasicMathMoLang extends BaseMoLangContext {

	public BasicMathMoLang() {
		registerFunction(MoLangNamespaceUtils.withMathNamespace("round"), (args, runtime) -> {
			double value = MoLangMathUtils.toDouble(args, 0);
			int decimals = MoLangMathUtils.toDouble(args, 1, 0.0).intValue();
			double factor = Math.pow(10, decimals);
			return Math.round(value * factor) / factor;
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("floor"), (args, runtime) -> Math.floor(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("ceil"), (args, runtime) -> Math.ceil(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("abs"), (args, runtime) -> Math.abs(MoLangMathUtils.toDouble(args, 0)));

		registerFunction(MoLangNamespaceUtils.withMathNamespace("clamp"), (args, runtime) -> {
			double value = MoLangMathUtils.toDouble(args, 0);
			double min = MoLangMathUtils.toDouble(args, 1);
			double max = MoLangMathUtils.toDouble(args, 2);
			return Math.max(min, Math.min(max, value));
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("min"), (args, runtime) -> args.stream().mapToDouble(MoLangMathUtils::toDouble).min().orElse(0.0));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("max"), (args, runtime) -> args.stream().mapToDouble(MoLangMathUtils::toDouble).max().orElse(0.0));

		registerFunction(MoLangNamespaceUtils.withMathNamespace("mod"), (args, runtime) -> {
			double a = MoLangMathUtils.toDouble(args, 0);
			double b = MoLangMathUtils.toDouble(args, 1);
			return a % b;
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("trunc"), (args, runtime) -> {
			double value = MoLangMathUtils.toDouble(args, 0);
			return value < 0 ? Math.ceil(value) : Math.floor(value);
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("lerp"), (args, runtime) -> {
			double a = MoLangMathUtils.toDouble(args, 0);
			double b = MoLangMathUtils.toDouble(args, 1);
			double t = MoLangMathUtils.toDouble(args, 2);
			return a + (b - a) * t;
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("lerprotate"), (args, runtime) -> {
			double a = MoLangMathUtils.toDouble(args, 0);
			double b = MoLangMathUtils.toDouble(args, 1);
			double t = MoLangMathUtils.toDouble(args, 2);
			double delta = ((b - a + 180) % 360) - 180;
			return a + delta * t;
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("hermite_blend"), (args, runtime) -> {
			double t = MoLangMathUtils.toDouble(args, 0);
			return t * t * (3 - 2 * t);
		});
	}
}
