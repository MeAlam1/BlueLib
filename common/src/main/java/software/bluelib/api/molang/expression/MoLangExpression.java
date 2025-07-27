/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.expression;

import java.util.ArrayList;
import java.util.List;
import software.bluelib.api.molang.MoLangRuntime;

public class MoLangExpression {

	private final String raw;

	private MoLangExpression(String pRaw) {
		this.raw = pRaw.trim();
	}

	public static MoLangExpression parse(String pRaw) {
		return new MoLangExpression(pRaw);
	}

	public Object evaluate(MoLangRuntime pRuntime) {
		if (raw.startsWith("q.")) {
			String expr = raw.substring(2);
			if (expr.endsWith(")")) {
				// Function call: q.foo(1, 2)
				String name = expr.substring(0, expr.indexOf('('));
				String argsRaw = expr.substring(expr.indexOf('(') + 1, expr.length() - 1);
				List<Object> args = parseArguments(argsRaw, pRuntime);
				return pRuntime.callFunction(name, args);
			} else {
				// Variable: q.var
				return pRuntime.getVariable(expr);
			}
		}
		if (raw.equalsIgnoreCase("true")) {
			return true;
		}
		if (raw.equalsIgnoreCase("false")) {
			return false;
		}
		try {
			return Double.parseDouble(raw);
		} catch (NumberFormatException e) {
			return raw;
		}
	}

	private List<Object> parseArguments(String pArgsRaw, MoLangRuntime pRuntime) {
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
