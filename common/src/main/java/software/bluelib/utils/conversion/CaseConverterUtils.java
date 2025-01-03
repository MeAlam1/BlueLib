// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.conversion;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} for converting strings between various naming conventions:
 * camelCase, PascalCase, snake_case, kebab-case, UPPER_SNAKE_CASE, Train-Case, flatcase, and COBOL-CASE.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #toCamelCase(String)} - Converts input to camelCase.</li>
 * <li>{@link #toPascalCase(String)} - Converts input to PascalCase.</li>
 * <li>{@link #toSnakeCase(String)} - Converts input to snake_case.</li>
 * <li>{@link #toKebabCase(String)} - Converts input to kebab-case.</li>
 * <li>{@link #toUpperSnakeCase(String)} - Converts input to UPPER_SNAKE_CASE.</li>
 * <li>{@link #toTrainCase(String)} - Converts input to Train-Case.</li>
 * <li>{@link #toFlatcase(String)} - Converts input to flatcase.</li>
 * <li>{@link #toCobolCase(String)} - Converts input to COBOL-CASE.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class CaseConverterUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private CaseConverterUtils() {}

    /**
     * A {@link String} that converts a given {@link String} to camelCase.
     * <p>
     * If the input is in PascalCase, snake_case, or kebab-case, it will be converted accordingly.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The camelCase version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a given {@link String} to PascalCase.
     * <p>
     * If the input is in camelCase, snake_case, or kebab-case, it will be converted accordingly.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The PascalCase version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a given {@link String} to snake_case.
     * <p>
     * It converts camelCase, PascalCase, and kebab-case to snake_case by adding underscores where appropriate.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The snake_case version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a given {@link String} to kebab-case.
     * <p>
     * It converts camelCase, PascalCase, and snake_case to a kebab-case by adding hyphens where appropriate.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The kebab-case version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a given {@link String} to UPPER_SNAKE_CASE.
     * <p>
     * Converts camelCase, PascalCase, snake_case, and kebab-case to UPPER_SNAKE_CASE by adding underscores
     * and converting all letters to uppercase.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The UPPER_SNAKE_CASE version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String toUpperSnakeCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toUpperSnakeCase is null or empty.", true);
            return pInput;
        }

        String result = toSnakeCase(pInput);
        return result.toUpperCase();
    }

    /**
     * A {@link String} that converts a given {@link String} to Train-Case.
     * <p>
     * Converts camelCase, PascalCase, snake_case, and kebab-case to Train-Case by adding hyphens and
     * capitalizing on each word.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The Train-Case version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String toTrainCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toTrainCase is null or empty.", true);
            return pInput;
        }

        String result = toKebabCase(pInput).replace("-", " ");
        return toCamelCase(result).replace(" ", "-");
    }

    /**
     * A {@link String} that converts a given {@link String} to flatcase.
     * <p>
     * Converts all cases to flatcase by removing any delimiters and converting to lowercase.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The flatcase version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String toFlatcase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toFlatcase is null or empty.", true);
            return pInput;
        }

        return pInput.replaceAll("[_-]", "").toLowerCase();
    }

    /**
     * A {@link String} that converts a given {@link String} to COBOL-CASE.
     * <p>
     * Converts camelCase, PascalCase, snake_case, and kebab-case to COBOL-CASE by making all letters uppercase
     * and replacing spaces with hyphens.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The COBOL-CASE version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String toCobolCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log(BaseLogLevel.WARNING, "Input for toCobolCase is null or empty.", true);
            return pInput;
        }

        String result = toKebabCase(pInput);
        return result.toUpperCase();
    }

    /**
     * A helper method that converts strings using a specified delimiter.
     * <p>
     * This method capitalizes the first letter of each word and joins them using the specified delimiter.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @param pDelim {@link String} - The delimiter used to split the input string.
     * @param pCamel {@code boolean} - Indicates if the conversion is to camelCase.
     * @return The converted string.
     * @author MeAlam
     * @since 1.0.0
     */
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
