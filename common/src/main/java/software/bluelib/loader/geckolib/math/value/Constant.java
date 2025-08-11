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
public record Constant(double value) implements MathValue {

	@Override
	public double get() {
		return this.value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
