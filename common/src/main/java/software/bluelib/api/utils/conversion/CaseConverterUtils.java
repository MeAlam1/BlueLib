// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.conversion;

import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class CaseConverterUtils {

    private CaseConverterUtils() {}

    public static String toCamelCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, "Input for toCamelCase is null or empty.", true);
            return pInput;
        }

        if (Character.isUpperCase(pInput.charAt(0)) && !pInput.contains("_") && !pInput.contains("-")) {
            BaseLogger.log(BaseLogLevel.INFO, "Input detected as PascalCase.", true);
            return pInput.substring(0, 1).toLowerCase() + pInput.substring(1);
        }

        if (pInput.contains("_")) {
            BaseLogger.log(BaseLogLevel.INFO, "Input detected as snake_case.", true);
            return convertUsingDelimiter(pInput, "_", true);
        }

        if (pInput.contains("-")) {
            BaseLogger.log(BaseLogLevel.INFO, "Input detected as kebab-case.", true);
            return convertUsingDelimiter(pInput, "-", true);
        }

        BaseLogger.log(BaseLogLevel.ERROR, "Input case is not recognized.", true);
        return pInput;
    }

    public static String toPascalCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toPascalCase is null or empty.", true);
            return pInput;
        }

        if (!pInput.contains("_") && !pInput.contains("-") && Character.isLowerCase(pInput.charAt(0))) {
            BaseLogger.log(BaseLogLevel.INFO, "Input detected as camelCase.", true);
            return pInput.substring(0, 1).toUpperCase() + pInput.substring(1);
        }

        if (pInput.contains("_")) {
            BaseLogger.log(BaseLogLevel.INFO, "Input detected as snake_case.", true);
            return convertUsingDelimiter(pInput, "_", false);
        }

        if (pInput.contains("-")) {
            BaseLogger.log(BaseLogLevel.INFO, "Input detected as kebab-case.", true);
            return convertUsingDelimiter(pInput, "-", false);
        }

        BaseLogger.log(BaseLogLevel.ERROR, "Input case is not recognized.", true);
        return pInput;
    }

    public static String toSnakeCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toSnakeCase is null or empty.", true);
            return pInput;
        }

        String result = pInput.replaceAll("([a-z])([A-Z])", "$1_$2");
        result = result.toLowerCase();
        result = result.replace("-", "_");

        BaseLogger.log(BaseLogLevel.SUCCESS, "Converted to snake_case: " + result, true);
        return result;
    }

    public static String toKebabCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toKebabCase is null or empty.", true);
            return pInput;
        }

        String result = pInput.replaceAll("([a-z])([A-Z])", "$1-$2");
        result = result.toLowerCase();
        result = result.replace("_", "-");

        BaseLogger.log(BaseLogLevel.SUCCESS, "Converted to kebab-case: " + result, true);
        return result;
    }

    public static String toUpperSnakeCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toUpperSnakeCase is null or empty.", true);
            return pInput;
        }

        String result = toSnakeCase(pInput);
        return result.toUpperCase();
    }

    public static String toTrainCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toTrainCase is null or empty.", true);
            return pInput;
        }

        String result = toKebabCase(pInput).replace("-", " ");
        return toCamelCase(result).replace(" ", "-");
    }

    public static String toFlatcase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toFlatcase is null or empty.", true);
            return pInput;
        }

        return pInput.replaceAll("[_-]", "").toLowerCase();
    }

    public static String toCobolCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toCobolCase is null or empty.", true);
            return pInput;
        }

        String result = toKebabCase(pInput);
        return result.toUpperCase();
    }

    private static String convertUsingDelimiter(String pInput, String pDelim, boolean pCamel) {
        String[] parts = pInput.split(pDelim);
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (pCamel && sb.isEmpty()) {
                sb.append(part.substring(0, 1).toLowerCase());
            } else {
                sb.append(part.substring(0, 1).toUpperCase());
            }
            sb.append(part.substring(1).toLowerCase());
        }

        return sb.toString();
    }
}
