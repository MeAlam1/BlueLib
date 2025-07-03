/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.value;

import software.bluelib.oldLoader.loading.math.MathValue;

public record BooleanNegate(MathValue value) implements MathValue {

	@Override
	public double get() {
		return this.value.get() == 0 ? 1 : 0;
	}

	@Override
	public boolean isMutable() {
		return this.value.isMutable();
	}

	@Override
	public String toString() {
		return "!" + this.value.toString();
	}
}
