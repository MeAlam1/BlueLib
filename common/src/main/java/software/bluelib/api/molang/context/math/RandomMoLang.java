/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.math;

import java.util.concurrent.ThreadLocalRandom;
import software.bluelib.api.molang.MoLangNamespaceUtils;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class RandomMoLang extends BaseMoLangContext {

	public RandomMoLang() {
		registerFunction(MoLangNamespaceUtils.withMathNamespace("random"), (args, runtime) -> {
			if (args == null || args.isEmpty()) {
				return ThreadLocalRandom.current().nextDouble();
			}
			if (args.size() == 1) {
				double bound = MoLangMathUtils.toDouble(args.getFirst());
				if (bound <= 0) {
					return 0.0;
				}
				return ThreadLocalRandom.current().nextDouble(bound);
			}
			double min = MoLangMathUtils.toDouble(args.get(0));
			double max = MoLangMathUtils.toDouble(args.get(1));
			if (min > max) {
				double temp = min;
				min = max;
				max = temp;
			}
			if (min == max) {
				return min;
			}
			return ThreadLocalRandom.current().nextDouble(min, max);
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("random_int"), (args, runtime) -> {
			int min = MoLangMathUtils.toDouble(args, 0).intValue();
			int max = MoLangMathUtils.toDouble(args, 1).intValue();
			if (min > max) {
				int temp = min;
				min = max;
				max = temp;
			}
			if (min == max) {
				return min;
			}
			return ThreadLocalRandom.current().nextInt(min, max + 1);
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("die_roll"), (args, runtime) -> {
			int sides = MoLangMathUtils.toDouble(args, 0).intValue();
			if (sides <= 0) {
				throw new IllegalArgumentException("Number of die sides must be positive, got: " + sides);
			}
			return 1 + ThreadLocalRandom.current().nextDouble() * sides;
		});

		registerFunction(MoLangNamespaceUtils.withMathNamespace("die_roll_integer"), (args, runtime) -> {
			int sides = MoLangMathUtils.toDouble(args, 0).intValue();
			if (sides <= 0) {
				throw new IllegalArgumentException("Number of die sides must be positive, got: " + sides);
			}
			return 1 + ThreadLocalRandom.current().nextInt(sides);
		});
	}
}
