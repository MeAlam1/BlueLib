/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.value;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLangRuntime;

public record MoLangExpression(@NotNull String raw) implements MoLangValue {

	public MoLangExpression(@NotNull String raw) {
		this.raw = raw.trim();
	}

	@NotNull
	public static MoLangExpression parse(@NotNull String pRaw) {
		return new MoLangExpression(pRaw);
	}

	@Nullable
	@Override
	public Object evaluate(@NotNull MoLangRuntime pRuntime) {
		if (raw.startsWith("query.")) {
			String remapped = "q." + raw.substring("query.".length());
			return new MoLangExpression(remapped).evaluate(pRuntime);
		}

		if (raw.startsWith("q.")) {
			String expr = raw.substring(2); // Strip `q.`

			// Delegate q.math.* to math.*
			if (expr.startsWith("math.")) {
				return new MoLangExpression(expr).evaluate(pRuntime);
			}

			return getObject(pRuntime, expr);
		}

		// Direct access: math.foo() or math.var
		if (raw.startsWith("math.")) {
			return getObject(pRuntime, raw);
		}

		if (raw.equalsIgnoreCase("true")) return true;
		if (raw.equalsIgnoreCase("false")) return false;

		try {
			return Double.parseDouble(raw);
		} catch (NumberFormatException e) {
			return raw;
		}
	}

	@Nullable
	private Object getObject(@NotNull MoLangRuntime pRuntime, @NotNull String pRaw) {
		if (pRaw.endsWith(")")) {
			String name = pRaw.substring(0, pRaw.indexOf('('));
			String argsRaw = pRaw.substring(pRaw.indexOf('(') + 1, pRaw.length() - 1);
			List<Object> args = parseArguments(argsRaw, pRuntime);
			return pRuntime.callFunction(name, args);
		} else {
			return pRuntime.getVariable(pRaw);
		}
	}

	@NotNull
	private List<Object> parseArguments(@NotNull String pArgsRaw, @NotNull MoLangRuntime pRuntime) {
		List<Object> args = new ArrayList<>();
		int depth = 0;
		int start = 0;
		for (int i = 0; i < pArgsRaw.length(); i++) {
			char c = pArgsRaw.charAt(i);
			if (c == '(') depth++;
			else if (c == ')') depth--;
			else if (c == ',' && depth == 0) {
				String arg = pArgsRaw.substring(start, i).trim();
				if (!arg.isEmpty()) {
					args.add(MoLangExpression.parse(arg).evaluate(pRuntime));
				}
				start = i + 1;
			}
		}
		String lastArg = pArgsRaw.substring(start).trim();
		if (!lastArg.isEmpty()) {
			args.add(MoLangExpression.parse(lastArg).evaluate(pRuntime));
		}
		return args;
	}
}
