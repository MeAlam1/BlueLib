/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.function.limit;

import net.minecraft.util.Mth;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.function.MathFunction;

@WillBeDeprecated(since = "2.5.0", reason = "MathFunction will be made redundant with the new MoLang System.")
public final class ClampFunction extends MathFunction {

	private final MathValue value;
	private final MathValue min;
	private final MathValue max;

	public ClampFunction(MathValue... values) {
		super(values);

		this.value = values[0];
		this.min = values[1];
		this.max = values[2];
	}

	@Override
	public String getName() {
		return "math.clamp";
	}

	@Override
	public double compute() {
		return Mth.clamp(this.value.get(), this.min.get(), this.max.get());
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public MathValue[] getArgs() {
		return new MathValue[] { this.value, this.min, this.max };
	}
}
