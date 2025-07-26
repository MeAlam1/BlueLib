/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import org.jetbrains.annotations.NotNull;

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
				sum += toDouble(arg);
			}
			return sum;
		});

		registerFunction("subtract", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 0.0;
			double result = toDouble(arguments.getFirst());
			for (int i = 1; i < arguments.size(); i++) {
				result -= toDouble(arguments.get(i));
			}
			return result;
		});

		registerFunction("multiply", (arguments, runtime) -> {
			double result = 1;
			for (Object arg : arguments) {
				result *= toDouble(arg);
			}
			return result;
		});

		registerFunction("divide", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 0.0;
			double result = toDouble(arguments.getFirst());
			for (int i = 1; i < arguments.size(); i++) {
				double divisor = toDouble(arguments.get(i));
				if (divisor == 0) return 0.0;
				result /= divisor;
			}
			return result;
		});

		registerFunction("modulus", (arguments, runtime) -> {
			if (arguments.size() < 2) return 0.0;
			double a = toDouble(arguments.get(0));
			double b = toDouble(arguments.get(1));
			if (b == 0) return 0.0;
			return a % b;
		});

		registerFunction("negate", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 0.0;
			return -toDouble(arguments.getFirst());
		});
	}

	private void Unary() {
		registerFunction("increment", (arguments, runtime) -> {
			if (arguments.isEmpty()) return 1.0;
			return toDouble(arguments.getFirst()) + 1;
		});

		registerFunction("decrement", (arguments, runtime) -> {
			if (arguments.isEmpty()) return -1.0;
			return toDouble(arguments.getFirst()) - 1;
		});
	}

	private void Comparison() {
		registerFunction("equals", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return toDouble(arguments.get(0)) == toDouble(arguments.get(1));
		});
		registerFunction("not_equals", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return toDouble(arguments.get(0)) != toDouble(arguments.get(1));
		});
		registerFunction("less_than", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return toDouble(arguments.get(0)) < toDouble(arguments.get(1));
		});
		registerFunction("less_than_or_equal", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return toDouble(arguments.get(0)) <= toDouble(arguments.get(1));
		});
		registerFunction("greater_than", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return toDouble(arguments.get(0)) > toDouble(arguments.get(1));
		});
		registerFunction("greater_than_or_equal", (arguments, runtime) -> {
			if (arguments.size() < 2) return false;
			return toDouble(arguments.get(0)) >= toDouble(arguments.get(1));
		});
	}

	private double toDouble(@NotNull Object pObj) {
		if (pObj instanceof Number number) return number.doubleValue();
		try {
			return Double.parseDouble(pObj.toString());
		} catch (Exception ignored) {
			return 0;
		}
	}
}
