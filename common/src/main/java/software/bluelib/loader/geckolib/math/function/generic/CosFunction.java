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

@WillBeDeprecated(since = "2.3.1", reason = "MathFunction will be made redundant with the new MoLang System.")
public final class CosFunction extends MathFunction {

	private final MathValue value;

	public CosFunction(MathValue... values) {
		super(values);

		this.value = values[0];
	}

	@Override
	public String getName() {
		return "math.cos";
	}

	@Override
	public double compute() {
		return Mth.cos((float) this.value.get() * Mth.DEG_TO_RAD);
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
