/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.function.misc;

import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.function.MathFunction;
import software.bluelib.loader.geckolib.math.value.Constant;

@WillBeDeprecated(since = "2.5.0", reason = "MathFunction will be made redundant with the new MoLang System.")
public final class PiFunction extends MathFunction {

	public PiFunction(MathValue... values) {
		super(values);
	}

	@Override
	public String getName() {
		return "math.pi";
	}

	@Override
	public double compute() {
		return Math.PI;
	}

	@Override
	public boolean isMutable(MathValue... values) {
		return false;
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { new Constant(Math.PI) };
	}
}
