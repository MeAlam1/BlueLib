/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.function.round;

import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.function.MathFunction;

@WillBeDeprecated(since = "2.5.0", reason = "MathFunction will be made redundant with the new MoLang System.")
public final class HermiteBlendFunction extends MathFunction {

	private final MathValue valueA;

	public HermiteBlendFunction(MathValue... values) {
		super(values);

		this.valueA = values[0];
	}

	@Override
	public String getName() {
		return "math.hermite_blend";
	}

	@Override
	public double compute() {
		final double value = this.valueA.get();

		return (3 * value * value) - (2 * value * value * value);
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { this.valueA };
	}
}
