/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.function.generic;

import net.minecraft.util.Mth;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.function.MathFunction;

@WillBeDeprecated(since = "2.5.0", reason = "MathFunction will be made redundant with the new MoLang System.")
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
