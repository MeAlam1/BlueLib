/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import software.bluelib.api.molang.context.math.MoLangMathUtils;

/*
 * TODO: Add
 * Arithmetic Operators
 * Comparison Operators
 * Logical Operators
 * Bitwise Operators
 * Assignment Operators
 * Unary Operators
 * SEPARATE METHODS FOR EACH OPERATOR
 */
public class OperatorMoLang extends BaseMoLangContext {

	public OperatorMoLang() {
		Arithmetic();
		Unary();
		Comparison();
	}

	private void Arithmetic() {
		registerFunction("add", (arguments, runtime) -> {
			double sum = 0;
			for (Object arg : arguments) {
				sum += MoLangMathUtils.toDouble(arg);
			}
			return sum;
		});

		registerFunction("subtract", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 0.0;
			double result = MoLangMathUtils.toDouble(arguments.getFirst());
			for (int i = 1; i < arguments.size(); i++) {
				result -= MoLangMathUtils.toDouble(arguments.get(i));
			}
			return result;
		});

		registerFunction("multiply", (arguments, runtime) -> {
			double result = 1;
			for (Object arg : arguments) {
				result *= MoLangMathUtils.toDouble(arg);
			}
			return result;
		});

		registerFunction("divide", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 0.0;
			double result = MoLangMathUtils.toDouble(arguments.getFirst());
			for (int i = 1; i < arguments.size(); i++) {
				double divisor = MoLangMathUtils.toDouble(arguments.get(i));
				if (divisor == 0) return 0.0;
				result /= divisor;
			}
			return result;
		});

		registerFunction("modulus", (arguments, runtime) -> {
			if (arguments.size() < 2) return 0.0;
			double a = MoLangMathUtils.toDouble(arguments.get(0));
			double b = MoLangMathUtils.toDouble(arguments.get(1));
			if (b == 0) return 0.0;
			return a % b;
		});

		registerFunction("negate", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 0.0;
			return -MoLangMathUtils.toDouble(arguments.getFirst());
		});
	}

	private void Unary() {
		registerFunction("increment", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 1.0;
			return MoLangMathUtils.toDouble(arguments.getFirst()) + 1;
		});

		registerFunction("decrement", (arguments, runtime) -> {
			if (arguments.isEmpty()) return -1.0;
			return MoLangMathUtils.toDouble(arguments.getFirst()) - 1;
		});
	}

	private void Comparison() {
		registerFunction("equals", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return MoLangMathUtils.toDouble(arguments.get(0)).equals(MoLangMathUtils.toDouble(arguments.get(1)));
		});
		registerFunction("not_equals", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return !MoLangMathUtils.toDouble(arguments.get(0)).equals(MoLangMathUtils.toDouble(arguments.get(1)));
		});
		registerFunction("less_than", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return MoLangMathUtils.toDouble(arguments.get(0)) < MoLangMathUtils.toDouble(arguments.get(1));
		});
		registerFunction("less_than_or_equal", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return MoLangMathUtils.toDouble(arguments.get(0)) <= MoLangMathUtils.toDouble(arguments.get(1));
		});
		registerFunction("greater_than", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return MoLangMathUtils.toDouble(arguments.get(0)) > MoLangMathUtils.toDouble(arguments.get(1));
		});
		registerFunction("greater_than_or_equal", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return MoLangMathUtils.toDouble(arguments.get(0)) >= MoLangMathUtils.toDouble(arguments.get(1));
		});
	}
}
