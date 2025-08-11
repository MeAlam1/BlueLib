/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.function.round;

import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.function.MathFunction;

@WillBeDeprecated(since = "2.3.1", reason = "MathFunction will be made redundant with the new MoLang System.")
public final class LerpRotFunction extends MathFunction {

	private final MathValue min;
	private final MathValue max;
	private final MathValue delta;

	public LerpRotFunction(MathValue... values) {
		super(values);

		this.min = values[0];
		this.max = values[1];
		this.delta = values[2];
	}

	@Override
	public String getName() {
		return "math.lerprotate";
	}

	@Override
	public double compute() {
		return RenderUtils.lerpYaw(this.delta.get(), this.min.get(), this.max.get());
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
