/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math.function;

import java.util.StringJoiner;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.math.MathValue;

@WillBeDeprecated(since = "2.5.0", reason = "MathFunction will be made redundant with the new MoLang System.")
public abstract class MathFunction implements MathValue {

	private final boolean isMutable;
	private double cachedValue = Double.MIN_VALUE;

	protected MathFunction(MathValue... values) {
		validate(values);

		this.isMutable = isMutable(values);
	}

	public abstract String getName();

	@Override
	public final double get() {
		if (this.isMutable)
			return compute();

		if (this.cachedValue == Double.MIN_VALUE)
			this.cachedValue = compute();

		return this.cachedValue;
	}

	public abstract double compute();

	public boolean isMutable(MathValue... values) {
		for (MathValue value : values) {
			if (value.isMutable())
				return true;
		}

		return false;
	}

	public abstract int getMinArgs();

	public abstract MathValue[] getArgs();

	public void validate(MathValue... inputs) throws IllegalArgumentException {
		final int minArgs = getMinArgs();

		if (inputs.length < minArgs)
			throw new IllegalArgumentException(String.format("Function '%s' at least %s arguments. Only %s given!", getName(), minArgs, inputs.length));
	}

	@Override
	public final boolean isMutable() {
		return this.isMutable;
	}

	@Override
	public String toString() {
		final MathValue[] args = getArgs();
		final StringJoiner joiner = new StringJoiner(", ", "(", ")");

		for (MathValue arg : args) {
			joiner.add(arg.toString());
		}

		return getName() + joiner;
	}

	@FunctionalInterface
	public interface Factory<T extends MathFunction> {

		T create(MathValue... values);
	}
}
