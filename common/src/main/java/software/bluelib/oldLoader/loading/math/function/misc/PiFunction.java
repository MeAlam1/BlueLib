/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.function.misc;

import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.function.MathFunction;
import software.bluelib.oldLoader.loading.math.value.Constant;

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
