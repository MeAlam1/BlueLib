/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MoLangRuntime {

	@NotNull
	private final Map<String, MoLangContext> baseContexts = new HashMap<>();
	@NotNull
	private final Map<String, List<MoLangContext>> contextStack = new HashMap<>();

	public void registerContext(@NotNull String pKey, @NotNull MoLangContext pContext) {
		baseContexts.put(pKey, pContext);
	}

	public void pushContext(@NotNull String pKey, @NotNull MoLangContext pContext) {
		contextStack.computeIfAbsent(pKey, k -> new ArrayList<>()).add(pContext);
	}

	public void popContext(@NotNull String pKey) {
		List<MoLangContext> stack = contextStack.get(pKey);
		if (stack != null && !stack.isEmpty()) {
			stack.removeLast();
			if (stack.isEmpty()) {
				contextStack.remove(pKey);
			}
		}
	}

	private @Nullable MoLangContext getContext(@NotNull String pKey) {
		List<MoLangContext> stack = contextStack.get(pKey);
		if (stack != null && !stack.isEmpty()) {
			return stack.getLast();
		}
		return baseContexts.get(pKey);
	}

	public @Nullable Object evaluate(@NotNull String pExpression) {
		pExpression = pExpression.trim();

		if ((pExpression.startsWith("'") && pExpression.endsWith("'")) ||
				(pExpression.startsWith("\"") && pExpression.endsWith("\""))) {
			String inner = pExpression.substring(1, pExpression.length() - 1);
			if (looksLikeMoLang(inner)) {
				return evaluate(inner);
			}
			if (isNumeric(inner)) return Double.parseDouble(inner);
			return inner;
		}

		if (isNumeric(pExpression)) return Double.parseDouble(pExpression);
		if ("true".equalsIgnoreCase(pExpression)) return true;
		if ("false".equalsIgnoreCase(pExpression)) return false;

		int dotIdx = pExpression.indexOf('.');
		if (dotIdx > 0) {
			String contextKey = pExpression.substring(0, dotIdx);
			String rest = pExpression.substring(dotIdx + 1);

			MoLangContext context = getContext(contextKey);
			if (context == null) return null;

			int parenIdx = rest.indexOf('(');
			if (parenIdx > 0 && rest.endsWith(")")) {
				String funcName = rest.substring(0, parenIdx);
				String argStr = rest.substring(parenIdx + 1, rest.length() - 1);
				List<Object> args = parseArgs(argStr);
				return context.callFunction(funcName, args);
			} else {
				return context.getVariable(rest);
			}
		}
		return null;
	}

	private boolean looksLikeMoLang(@NotNull String pExpr) {
		return pExpr.matches("[a-zA-Z]\\w*\\.[\\w_]+(\\(.*\\))?");
	}

	private @NotNull List<Object> parseArgs(@NotNull String pArgStr) {
		List<Object> args = new ArrayList<>();
		int depth = 0;
		StringBuilder current = new StringBuilder();
		for (int i = 0; i < pArgStr.length(); i++) {
			char c = pArgStr.charAt(i);
			if (c == ',' && depth == 0) {
				args.add(evaluateNested(current.toString().trim()));
				current.setLength(0);
			} else {
				if (c == '(') depth++;
				if (c == ')') depth--;
				current.append(c);
			}
		}
		if (!current.isEmpty()) {
			args.add(evaluateNested(current.toString().trim()));
		}
		return args;
	}

	private Object evaluateNested(String pExpr) {
		return evaluate(pExpr);
	}

	private boolean isNumeric(@NotNull String pString) {
		try {
			Double.parseDouble(pString);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
