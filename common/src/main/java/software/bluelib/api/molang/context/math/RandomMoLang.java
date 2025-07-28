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
				return ThreadLocalRandom.current().nextDouble(MoLangMathUtils.toDouble(args.getFirst()));
			}
			double min = MoLangMathUtils.toDouble(args.get(0));
			double max = MoLangMathUtils.toDouble(args.get(1));
			return ThreadLocalRandom.current().nextDouble(min, max);
		});
	}
}
