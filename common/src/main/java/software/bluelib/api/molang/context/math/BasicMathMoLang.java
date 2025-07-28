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
			int decimals = (int) MoLangMathUtils.toDouble(args, 1, 0);
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
	}
}
