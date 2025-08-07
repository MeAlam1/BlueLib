/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.value;

import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;

@WillBeDeprecated(since = "2.3.1", reason = "MathValue will be made redundant with the new MoLang System.")
public record Ternary(MathValue condition, MathValue trueValue, MathValue falseValue) implements MathValue {

	@Override
	public double get() {
		return this.condition.get() != 0 ? this.trueValue.get() : this.falseValue.get();
	}

	@Override
	public boolean isMutable() {
		return this.condition.isMutable() || this.trueValue.isMutable() || this.falseValue.isMutable();
	}

	@Override
	public String toString() {
		return this.condition.toString() + " ? " + this.trueValue.toString() + " : " + this.falseValue.toString();
	}
}
