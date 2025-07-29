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

import java.util.concurrent.ThreadLocalRandom;

public class RandomMoLang extends BaseMoLangContext {

	public RandomMoLang() {
		registerFunction(MoLangNamespaceUtils.withMathNamespace("random"), (args, runtime) -> {
			if (args == null || args.isEmpty()) {
				return ThreadLocalRandom.current().nextDouble();
			}
			if (args.size() == 1) {
				return ThreadLocalRandom.current().nextDouble(MoLangMathUtils.toDouble(args.getFirst()));
			}
			double min = MoLangMathUtils.toDouble(args.get(0));
			double max = MoLangMathUtils.toDouble(args.get(1));
			return ThreadLocalRandom.current().nextDouble(min, max);
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("random_integer"), (args, runtime) -> {
			int min = (int) MoLangMathUtils.toDouble(args, 0);
			int max = (int) MoLangMathUtils.toDouble(args, 1);
			return ThreadLocalRandom.current().nextInt(min, max + 1);
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("die_roll"), (args, runtime) -> {
			int sides = (int) MoLangMathUtils.toDouble(args, 0);
			return 1 + ThreadLocalRandom.current().nextDouble() * sides;
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("die_roll_integer"), (args, runtime) -> {
			int sides = (int) MoLangMathUtils.toDouble(args, 0);
			return 1 + ThreadLocalRandom.current().nextInt(sides);
		});
	}
}
