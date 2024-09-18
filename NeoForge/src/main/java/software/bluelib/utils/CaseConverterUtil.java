package software.bluelib.utils;

import software.bluelib.utils.logging.BaseLogger;

public class CaseConverterUtil {

    public static String toCamelCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toCamelCase is null or empty.");
            return pInput;
        }

        String[] parts = pInput.split("_");
        StringBuilder camelCaseString = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                camelCaseString.append(parts[i].toLowerCase());
            } else {
                camelCaseString.append(toProperCase(parts[i]));
            }
        }
        BaseLogger.bluelibLogSuccess("Converted to camelCase: " + camelCaseString);
        return camelCaseString.toString();
    }

    public static String toPascalCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toPascalCase is null or empty.");
            return pInput;
        }

        String[] parts = pInput.split("_");
        StringBuilder pascalCaseString = new StringBuilder();
        for (String part : parts) {
            pascalCaseString.append(toProperCase(part));
        }
        BaseLogger.bluelibLogSuccess("Converted to PascalCase: " + pascalCaseString);
        return pascalCaseString.toString();
    }

    public static String toProperCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toProperCase is null or empty.");
            return pInput;
        }
        String result = pInput.substring(0, 1).toUpperCase() + pInput.substring(1).toLowerCase();
        BaseLogger.bluelibLogSuccess("Converted to ProperCase: " + result);
        return result;
    }

    public static String toSnakeCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toSnakeCase is null or empty.");
            return pInput;
        }

        StringBuilder snakeCaseString = new StringBuilder();
        for (int i = 0; i < pInput.length(); i++) {
            char c = pInput.charAt(i);
            if (Character.isUpperCase(c)) {
                if (!snakeCaseString.isEmpty()) {
                    snakeCaseString.append("_");
                }
                snakeCaseString.append(Character.toLowerCase(c));
            } else {
                snakeCaseString.append(c);
            }
        }
        BaseLogger.bluelibLogSuccess("Converted to snake_case: " + snakeCaseString);
        return snakeCaseString.toString();
    }

    public static String toLowerCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toLowerCase is null or empty.");
            return pInput;
        }
        String result = pInput.toLowerCase();
        BaseLogger.bluelibLogSuccess("Converted to lowercase: " + result);
        return result;
    }

    public static String toUpperCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toUpperCase is null or empty.");
            return pInput;
        }
        String result = pInput.toUpperCase();
        BaseLogger.bluelibLogSuccess("Converted to UPPERCASE: " + result);
        return result;
    }
}
