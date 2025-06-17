package software.bluelib.api.molang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoLangRuntime {

    private final Map<String, MoLangContext> contexts = new HashMap<>();

    public void registerContext(String pKey, MoLangContext pContext) {
        contexts.put(pKey, pContext);
    }

    public Object evaluate(String pExpression) {
        pExpression = pExpression.trim();

        // Literal Strings
        if ((pExpression.startsWith("'") && pExpression.endsWith("'")) ||
                (pExpression.startsWith("\"") && pExpression.endsWith("\""))) {
            return pExpression.substring(1, pExpression.length() - 1);
        }

        // Literal values
        if (isNumeric(pExpression)) return Double.parseDouble(pExpression);
        if ("true".equalsIgnoreCase(pExpression)) return true;
        if ("false".equalsIgnoreCase(pExpression)) return false;

        int dotIdx = pExpression.indexOf('.');
        if (dotIdx > 0) {
            String contextKey = pExpression.substring(0, dotIdx);
            String rest = pExpression.substring(dotIdx + 1);

            MoLangContext context = contexts.get(contextKey);
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

    private List<Object> parseArgs(String pArgStr) {
        List<Object> args = new ArrayList<>();
        int depth = 0;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < pArgStr.length(); i++) {
            char c = pArgStr.charAt(i);
            if (c == ',' && depth == 0) {
                args.add(evaluate(current.toString().trim()));
                current.setLength(0);
            } else {
                if (c == '(') depth++;
                if (c == ')') depth--;
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            args.add(evaluate(current.toString().trim()));
        }
        return args;
    }

    private boolean isNumeric(String pString) {
        try {
            Double.parseDouble(pString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
