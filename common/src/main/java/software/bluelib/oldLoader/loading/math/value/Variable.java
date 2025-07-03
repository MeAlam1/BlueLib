/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math.value;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleSupplier;
import software.bluelib.oldLoader.loading.math.MathValue;

public record Variable(String name, AtomicReference<DoubleSupplier> value) implements MathValue {

	public Variable(String name, double value) {
		this(name, () -> value);
	}

	public Variable(String name, DoubleSupplier value) {
		this(name, new AtomicReference<>(value));
	}

	@Override
	public double get() {
		try {
			return this.value.get().getAsDouble();
		} catch (Exception ex) {
			//BlueLibConstants.LOGGER.error("Attempted to use Molang variable for incompatible animatable type (" + this.name + "). An animation json needs to be fixed", ex.getMessage());

			return 0;
		}
	}

	public void set(final double value) {
		this.value.set(() -> value);
	}

	public void set(final DoubleSupplier value) {
		this.value.set(value);
	}

	@Override
	public String toString() {
		return this.name + "(" + this.value.get().getAsDouble() + ")";
	}
}
