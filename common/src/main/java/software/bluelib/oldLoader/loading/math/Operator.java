/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math;

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.chars.CharSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public record Operator(String symbol, int precedence, Operation operation) implements Comparable<Operator> {

	private static final Map<String, Operator> OPERATORS = new Object2ObjectOpenHashMap<>(14);
	private static final CharSet OPERATOR_SYMBOLS = Util.make(new CharOpenHashSet(15), set -> set.addAll(Arrays.asList('?', ':', ',')));
	private static int LONGEST_OPERATOR;

	public static final Operator ADD = register("+", 1, (a, b) -> a + b);
	public static final Operator SUB = register("-", 1, (a, b) -> a - b);
	public static final Operator MUL = register("*", 2, (a, b) -> a * b);
	public static final Operator DIV = register("/", 2, (a, b) -> b == 0 ? a : a / b);
	public static final Operator MOD = register("%", 2, (a, b) -> b == 0 ? a : a % b);
	public static final Operator POW = register("^", 3, Math::pow);
	public static final Operator AND = register("&&", 5, (a, b) -> a != 0 && b != 0 ? 1 : 0);
	public static final Operator OR = register("||", 5, (a, b) -> a != 0 || b != 0 ? 1 : 0);
	public static final Operator LT = register("<", 5, (a, b) -> a < b ? 1 : 0);
	public static final Operator LTE = register("<=", 5, (a, b) -> a <= b ? 1 : 0);
	public static final Operator GT = register(">", 5, (a, b) -> a > b ? 1 : 0);
	public static final Operator GTE = register(">=", 5, (a, b) -> a >= b ? 1 : 0);
	public static final Operator EQUAL = register("==", 5, (a, b) -> Math.abs(a - b) < 0.00001 ? 1 : 0);
	public static final Operator NOT_EQUAL = register("!=", 5, (a, b) -> Math.abs(a - b) >= 0.00001 ? 1 : 0);
	public static final Operator ASSIGN_VARIABLE = register("=", Integer.MAX_VALUE, (a, b) -> 0);

	public static Operator register(String symbol, int precedence, Operation operation) {
		final Operator operator = new Operator(symbol, precedence, operation);

		if (OPERATORS.put(symbol, operator) != null)
			throw new IllegalArgumentException("Attempting to register an already existing operator! '" + symbol + "'");

		for (char symbolChar : symbol.toCharArray()) {
			OPERATOR_SYMBOLS.add(symbolChar);
		}

		LONGEST_OPERATOR = Math.max(LONGEST_OPERATOR, symbol.length());

		return operator;
	}

	public static boolean isOperator(String symbol) {
		return OPERATORS.containsKey(symbol);
	}

	public static Optional<Operator> getOperatorFor(String symbol) {
		return Optional.ofNullable(OPERATORS.get(symbol));
	}

	public static int maxOperatorLength() {
		return LONGEST_OPERATOR;
	}

	public static boolean isOperativeSymbol(char symbol) {
		return OPERATOR_SYMBOLS.contains(symbol);
	}

	public double compute(double argA, double argB) {
		return this.operation.compute(argA, argB);
	}

	@Override
	public int compareTo(@NotNull Operator operator) {
		return Integer.compare(this.precedence, operator.precedence);
	}

	public boolean takesPrecedenceOver(Operator operator) {
		return compareTo(operator) > 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.symbol);
	}

	@FunctionalInterface
	public interface Operation {

		double compute(double argA, double argB);
	}
}
