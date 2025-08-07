/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.function.limit;

import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.function.MathFunction;

@WillBeDeprecated(since = "2.3.1", reason = "MathFunction will be made redundant with the new MoLang System.")
public final class MaxFunction extends MathFunction {

	private final MathValue valueA;
	private final MathValue valueB;

	public MaxFunction(MathValue... values) {
		super(values);

		this.valueA = values[0];
		this.valueB = values[1];
	}

	@Override
	public String getName() {
		return "math.max";
	}

	@Override
	public double compute() {
		return Math.max(this.valueA.get(), this.valueB.get());
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { this.valueA, this.valueB };
	}
}
