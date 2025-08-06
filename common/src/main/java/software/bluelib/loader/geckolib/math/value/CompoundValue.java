/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.value;

import java.util.StringJoiner;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;

@WillBeDeprecated(since = "2.5.0", reason = "MathValue will be made redundant with the new MoLang System.")
public record CompoundValue(MathValue[] subValues) implements MathValue {

	@Override
	public double get() {
		for (int i = 0; i < this.subValues.length - 1; i++) {
			this.subValues[i].get();
		}

		return this.subValues[this.subValues.length - 1].get();
	}

	@Override
	public String toString() {
		final StringJoiner joiner = new StringJoiner("; ");

		for (MathValue subValue : this.subValues) {
			joiner.add(subValue.toString());
		}

		return joiner.toString();
	}
}
