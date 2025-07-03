/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.math;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.exception.CompoundException;
import software.bluelib.oldLoader.loading.math.function.MathFunction;
import software.bluelib.oldLoader.loading.math.function.generic.*;
import software.bluelib.oldLoader.loading.math.function.limit.ClampFunction;
import software.bluelib.oldLoader.loading.math.function.limit.MaxFunction;
import software.bluelib.oldLoader.loading.math.function.limit.MinFunction;
import software.bluelib.oldLoader.loading.math.function.misc.PiFunction;
import software.bluelib.oldLoader.loading.math.function.misc.ToDegFunction;
import software.bluelib.oldLoader.loading.math.function.misc.ToRadFunction;
import software.bluelib.oldLoader.loading.math.function.random.DieRollFunction;
import software.bluelib.oldLoader.loading.math.function.random.DieRollIntegerFunction;
import software.bluelib.oldLoader.loading.math.function.random.RandomFunction;
import software.bluelib.oldLoader.loading.math.function.random.RandomIntegerFunction;
import software.bluelib.oldLoader.loading.math.function.round.*;
import software.bluelib.oldLoader.loading.math.value.*;

public class MathParser {

	private static final Pattern EXPRESSION_FORMAT = Pattern.compile("^[\\w\\s_+-/*%^&|<>=!?:.,()]+$");
	private static final Pattern WHITESPACE = Pattern.compile("\\s");
	private static final Pattern NUMERIC = Pattern.compile("^-?\\d+(\\.\\d+)?$");
	private static final Pattern VALID_DOUBLE = Pattern.compile("[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\d+)(\\.)?((\\d+)?)([eE][+-]?(\\d+))?)|(\\.(\\d+)([eE][+-]?(\\d+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\d+)))[fFdD]?))[\\x00-\\x20]*");
	private static final String MOLANG_RETURN = "return ";
	private static final String STATEMENT_DELIMITER = ";";
	private static final Map<String, MathFunction.Factory<?>> FUNCTION_FACTORIES = Util.make(new ConcurrentHashMap<>(18), map -> {
		map.put("math.abs", AbsFunction::new);
		map.put("math.acos", ACosFunction::new);
		map.put("math.asin", ASinFunction::new);
		map.put("math.atan", ATanFunction::new);
		map.put("math.atan2", ATan2Function::new);
		map.put("math.ceil", CeilFunction::new);
		map.put("math.clamp", ClampFunction::new);
		map.put("math.cos", CosFunction::new);
		map.put("math.die_roll", DieRollFunction::new);
		map.put("math.die_roll_integer", DieRollIntegerFunction::new);
		map.put("math.exp", ExpFunction::new);
		map.put("math.floor", FloorFunction::new);
		map.put("math.hermite_blend", HermiteBlendFunction::new);
		map.put("math.lerp", LerpFunction::new);
		map.put("math.lerprotate", LerpRotFunction::new);
		map.put("math.ln", LogFunction::new);
		map.put("math.max", MaxFunction::new);
		map.put("math.min", MinFunction::new);
		map.put("math.mod", ModFunction::new);
		map.put("math.pi", PiFunction::new);
		map.put("math.pow", PowFunction::new);
		map.put("math.random", RandomFunction::new);
		map.put("math.random_integer", RandomIntegerFunction::new);
		map.put("math.round", RoundFunction::new);
		map.put("math.sin", SinFunction::new);
		map.put("math.sqrt", SqrtFunction::new);
		map.put("math.to_deg", ToDegFunction::new);
		map.put("math.to_rad", ToRadFunction::new);
		map.put("math.trunc", TruncateFunction::new);
	});

	public static boolean isFunctionRegistered(String name) {
		return FUNCTION_FACTORIES.containsKey(name);
	}

	public static void registerFunction(String name, MathFunction.Factory<?> factory) {
		//if (FUNCTION_FACTORIES.put(name, factory) != null)
		//BlueLibConstants.LOGGER.log(Level.WARN, "Duplicate registration of MathFunction: '" + name + "'. Ignore if intentional override");

		//BlueLibConstants.LOGGER.log(Level.DEBUG, "Registered MathFunction '" + name + "'");
	}

	@Nullable
	public static <T extends MathFunction> T buildFunction(String name, MathValue... values) {
		if (!FUNCTION_FACTORIES.containsKey(name))
			return null;

		return (T) FUNCTION_FACTORIES.get(name).create(values);
	}

	public static void registerVariable(Variable variable) {
		MoLangQueries.registerVariable(variable);
	}

	public static Variable getVariableFor(String name) {
		return MoLangQueries.getVariableFor(name);
	}

	public static void setVariable(String name, DoubleSupplier value) {
		getVariableFor(name).set(value);
	}

	public static MathValue parseJson(JsonElement element) {
		if (!(element instanceof JsonPrimitive primitive) || primitive.isBoolean())
			throw new CompoundException("Bad formatting on Molang expression, expected single value, received: " + element.getClass().getSimpleName());

		if (primitive.isNumber())
			return new Constant(primitive.getAsDouble());

		if (primitive.isString()) {
			String value = primitive.getAsString();

			if (VALID_DOUBLE.matcher(value).matches())
				return new Constant(Double.parseDouble(value));

			return compileMolang(value);
		}

		return new Constant(0);
	}

	public static MathValue compileMolang(String expression) {
		if (expression.startsWith(MOLANG_RETURN)) {
			expression = expression.substring(MOLANG_RETURN.length());

			if (expression.contains(STATEMENT_DELIMITER))
				expression = expression.substring(0, expression.indexOf(STATEMENT_DELIMITER));
		} else if (expression.contains(STATEMENT_DELIMITER)) {
			final String[] subExpressions = expression.split(STATEMENT_DELIMITER);
			final List<MathValue> subValues = new ObjectArrayList<>(subExpressions.length);

			for (String subExpression : subExpressions) {
				boolean isReturn = subExpression.startsWith(MOLANG_RETURN);

				if (isReturn)
					subExpression = subExpression.substring(MOLANG_RETURN.length());

				subValues.add(compileExpression(subExpression));

				if (isReturn)
					break;
			}

			return new CompoundValue(subValues.toArray(new MathValue[0]));
		}

		return compileExpression(expression);
	}

	public static MathValue compileExpression(String expression) {
		try {
			return parseSymbols(compileSymbols(decomposeExpression(expression)));
		} catch (CompoundException ex) {
			throw ex.withMessage("Failed to parse expression '" + expression + "'");
		}
	}

	public static char[] decomposeExpression(String expression) throws CompoundException {
		if (!EXPRESSION_FORMAT.matcher(expression).matches())
			throw new CompoundException("Invalid characters found in expression: '" + expression + "'");

		final char[] chars = WHITESPACE.matcher(expression).replaceAll("").toLowerCase(Locale.ROOT).toCharArray();
		int groupState = 0;

		for (char character : chars) {
			if (character == '(') {
				groupState++;
			} else if (character == ')') {
				groupState--;
			}

			if (groupState < 0)
				throw new CompoundException("Closing parenthesis before opening parenthesis in expression '" + expression + "'");
		}

		if (groupState != 0)
			throw new CompoundException("Uneven parenthesis in expression, each opening brace must have a pairing close brace '" + expression + "'");

		return chars;
	}

	@Nullable
	protected static String tryMerBlueperativeSymbols(char[] chars, int index) {
		char ch = chars[index];

		if (!Operator.isOperativeSymbol(ch))
			return null;

		int maxLength = Math.min(chars.length - index, Operator.maxOperatorLength());

		for (int length = maxLength; length > 0; length--) {
			String testOperator = String.copyValueOf(chars, index, length);

			if (Operator.isOperator(testOperator))
				return testOperator;
		}

		if (ch == '?' || ch == ':' || ch == ',')
			return String.valueOf(ch);

		return null;
	}

	public static List<Either<String, List<MathValue>>> compileSymbols(char[] chars) {
		final List<Either<String, List<MathValue>>> symbols = new ObjectArrayList<>();
		final StringBuilder buffer = new StringBuilder();
		int lastSymbolIndex = -1;

		for (int i = 0; i < chars.length; i++) {
			final char ch = chars[i];

			if (ch == '-' && buffer.isEmpty() && (symbols.isEmpty() || lastSymbolIndex == symbols.size() - 1)) {
				buffer.append(ch);

				continue;
			}

			final String operator = tryMerBlueperativeSymbols(chars, i);

			if (operator != null) {
				i += operator.length() - 1;

				if (!buffer.isEmpty())
					symbols.add(Either.left(buffer.toString()));

				lastSymbolIndex = symbols.size();

				symbols.add(Either.left(operator));
				buffer.setLength(0);
			} else if (ch == '(') {
				if (!buffer.isEmpty()) {
					symbols.add(Either.left(buffer.toString()));
					buffer.setLength(0);
				}

				List<MathValue> subValues = new ObjectArrayList<>();
				int groupState = 1;

				for (int j = i + 1; j < chars.length; j++) {
					final char groupChar = chars[j];

					if (groupChar == '(') {
						groupState++;
					} else if (groupChar == ')') {
						groupState--;
					} else if (groupChar == ',' && groupState == 1) {
						subValues.add(parseSymbols(compileSymbols(buffer.toString().toCharArray())));
						buffer.setLength(0);

						continue;
					}

					if (groupState == 0) {
						if (!buffer.isEmpty())
							subValues.add(parseSymbols(compileSymbols(buffer.toString().toCharArray())));

						i = j;

						symbols.add(Either.right(subValues));
						buffer.setLength(0);

						break;
					} else {
						buffer.append(groupChar);
					}
				}
			} else {
				buffer.append(ch);
			}
		}

		if (!buffer.isEmpty())
			symbols.add(Either.left(buffer.toString()));

		return symbols;
	}

	public static MathValue parseSymbols(List<Either<String, List<MathValue>>> symbols) throws CompoundException {
		if (symbols.size() == 2) {
			Optional<String> prefix = symbols.getFirst().left().filter(left -> left.startsWith("-") || left.startsWith("!") || isFunctionRegistered(left));
			Optional<List<MathValue>> group = symbols.get(1).right();

			if (prefix.isPresent() && group.isPresent())
				return compileFunction(prefix.get(), group.get());
		}

		MathValue value = compileValue(symbols);

		if (value != null)
			return value;

		throw new CompoundException("Unable to parse compiled symbols from expression: " + symbols);
	}

	@Nullable
	protected static MathValue compileValue(List<Either<String, List<MathValue>>> symbols) throws CompoundException {
		if (symbols.size() == 1)
			return compileSingleValue(symbols.getFirst());

		Ternary ternary = compileTernary(symbols);

		if (ternary != null)
			return ternary;

		return compileCalculation(symbols);
	}

	@Nullable
	protected static MathValue compileSingleValue(Either<String, List<MathValue>> symbol) throws CompoundException {
		if (symbol.right().isPresent())
			return new Group(symbol.right().get().getFirst());

		return symbol.left().map(string -> {
			if (string.startsWith("!"))
				return new BooleanNegate(compileSingleValue(Either.left(string.substring(1))));

			if (isNumeric(string))
				return new Constant(Double.parseDouble(string));

			if (isLikelyVariable(string)) {
				if (string.startsWith("-"))
					return new Negative(getVariableFor(string.substring(1)));

				return getVariableFor(string);
			}

			if (isFunctionRegistered(string))
				return compileFunction(string, List.of());

			return null;
		}).orElse(null);
	}

	@Nullable
	protected static MathValue compileCalculation(List<Either<String, List<MathValue>>> symbols) throws CompoundException {
		final int symbolCount = symbols.size();
		int operatorIndex = -1;
		Operator lastOperator = null;

		for (int i = 1; i < symbolCount; i++) {
			Operator operator = symbols.get(i).left()
					.filter(Operator::isOperator)
					.map(MathParser::getOperatorFor).orElse(null);

			if (operator == null)
				continue;

			if (operator == Operator.ASSIGN_VARIABLE) {
				if (!(parseSymbols(symbols.subList(0, i)) instanceof Variable variable))
					throw new CompoundException("Attempted to assign a value to a non-variable");

				return new VariableAssignment(variable, parseSymbols(symbols.subList(i + 1, symbolCount)));
			}

			if (lastOperator == null || !operator.takesPrecedenceOver(lastOperator)) {
				operatorIndex = i;
				lastOperator = operator;
			} else {
				break;
			}
		}

		return lastOperator == null ? null : new Calculation(lastOperator, parseSymbols(symbols.subList(0, operatorIndex)), parseSymbols(symbols.subList(operatorIndex + 1, symbolCount)));
	}

	@Nullable
	protected static Ternary compileTernary(List<Either<String, List<MathValue>>> symbols) throws CompoundException {
		final int symbolCount = symbols.size();

		if (symbolCount < 3)
			return null;

		Supplier<MathValue> condition = null;
		Supplier<MathValue> ifTrue = null;
		int ternaryState = 0;
		int lastColon = -1;
		int queryIndex = -1;

		for (int i = 0; i < symbolCount; i++) {
			final int i2 = i;
			final String string = symbols.get(i).left().orElse(null);

			if ("?".equals(string)) {
				if (condition == null) {
					condition = () -> parseSymbols(symbols.subList(0, i2));
					queryIndex = i2 + 1;
				}

				ternaryState++;
			} else if (":".equals(string)) {
				if (ternaryState == 1 && ifTrue == null && queryIndex > 0) {
					final int queryIndex2 = queryIndex;
					ifTrue = () -> parseSymbols(symbols.subList(queryIndex2, i2));
				}

				ternaryState--;
				lastColon = i;
			}
		}

		if (ternaryState == 0 && condition != null && ifTrue != null && lastColon < symbolCount - 1)
			return new Ternary(condition.get(), ifTrue.get(), parseSymbols(symbols.subList(lastColon + 1, symbolCount)));

		return null;
	}

	@Nullable
	protected static MathValue compileFunction(String name, List<MathValue> args) throws CompoundException {
		if (name.startsWith("!")) {
			if (name.length() == 1)
				return new BooleanNegate(args.getFirst());

			return new BooleanNegate(compileFunction(name.substring(1), args));
		}

		if (name.startsWith("-")) {
			if (name.length() == 1)
				return new Negative(args.getFirst());

			return new Negative(compileFunction(name.substring(1), args));
		}

		if (!isFunctionRegistered(name))
			return null;

		return buildFunction(name, args.toArray(new MathValue[0]));
	}

	@Deprecated(forRemoval = true)
	public static boolean isOperativeSymbol(char symbol) {
		return isOperativeSymbol(String.valueOf(symbol));
	}

	@Deprecated(forRemoval = true)
	public static boolean isOperativeSymbol(@NotNull String symbol) {
		return Operator.isOperator(symbol) || symbol.equals("?") || symbol.equals(":");
	}

	public static boolean isNumeric(String string) {
		return NUMERIC.matcher(string).matches();
	}

	protected static Operator getOperatorFor(String op) throws CompoundException {
		return Operator.getOperatorFor(op).orElseThrow(() -> new CompoundException("Unknown operator symbol '" + op + "'"));
	}

	@Deprecated(forRemoval = true)
	protected static boolean isQueryOrFunctionName(String string) {
		return !isNumeric(string) && !isOperativeSymbol(string);
	}

	protected static boolean isLikelyVariable(String string) {
		if (MoLangQueries.isExistingVariable(string))
			return true;

		return !isNumeric(string) && !isFunctionRegistered(string) && !Operator.isOperator(string) && !string.equals("?") && !string.equals(":");
	}
}
