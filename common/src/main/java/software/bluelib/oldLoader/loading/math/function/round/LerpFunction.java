/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.function.round;

import net.minecraft.util.Mth;
import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.function.MathFunction;

public final class LerpFunction extends MathFunction {

	private final MathValue min;
	private final MathValue max;
	private final MathValue delta;

	public LerpFunction(MathValue... values) {
		super(values);

		this.min = values[0];
		this.max = values[1];
		this.delta = values[2];
	}

	@Override
	public String getName() {
		return "math.lerp";
	}

	@Override
	public double compute() {
		return Mth.lerp(this.delta.get(), this.min.get(), this.max.get());
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { this.min, this.max, this.delta };
	}
}
