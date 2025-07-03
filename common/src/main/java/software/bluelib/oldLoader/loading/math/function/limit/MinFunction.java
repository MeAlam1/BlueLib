/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.function.limit;

import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.function.MathFunction;

public final class MinFunction extends MathFunction {

	private final MathValue valueA;
	private final MathValue valueB;

	public MinFunction(MathValue... values) {
		super(values);

		this.valueA = values[0];
		this.valueB = values[1];
	}

	@Override
	public String getName() {
		return "math.min";
	}

	@Override
	public double compute() {
		return Math.min(this.valueA.get(), this.valueB.get());
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
