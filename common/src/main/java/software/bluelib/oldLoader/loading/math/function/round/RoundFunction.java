/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.function.round;

import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.function.MathFunction;

public final class RoundFunction extends MathFunction {

	private final MathValue value;

	public RoundFunction(MathValue... values) {
		super(values);

		this.value = values[0];
	}

	@Override
	public String getName() {
		return "math.round";
	}

	@Override
	public double compute() {
		return Math.round(this.value.get());
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { this.value };
	}
}
