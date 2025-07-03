/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.function.generic;

import net.minecraft.util.Mth;
import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.function.MathFunction;

public final class ATan2Function extends MathFunction {

	private final MathValue y;
	private final MathValue x;

	public ATan2Function(MathValue... values) {
		super(values);

		this.y = values[0];
		this.x = values[1];
	}

	@Override
	public String getName() {
		return "math.atan2";
	}

	@Override
	public double compute() {
		return Math.atan2(this.y.get(), this.x.get()) * Mth.RAD_TO_DEG;
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { this.y, this.x };
	}
}
