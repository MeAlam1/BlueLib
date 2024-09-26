// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.conversion;

import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code class} for converting strings between various naming conventions:
 * camelCase, PascalCase, snake_case, and kebab-case.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #toCamelCase(String)} - Converts input to camelCase.</li>
 *   <li>{@link #toPascalCase(String)} - Converts input to PascalCase.</li>
 *   <li>{@link #toSnakeCase(String)} - Converts input to snake_case.</li>
 *   <li>{@link #toKebabCase(String)} - Converts input to kebab-case.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class CaseConverterUtils {

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
            BaseLogger.log("Input for toCamelCase is null or empty.");
            return pInput;
        }

        if (Character.isUpperCase(pInput.charAt(0)) && !pInput.contains("_") && !pInput.contains("-")) {
            BaseLogger.log("Input detected as PascalCase.");
            return pInput.substring(0, 1).toLowerCase() + pInput.substring(1);
        }

        if (pInput.contains("_")) {
            BaseLogger.log("Input detected as snake_case.");
            return convertUsingDelimiter(pInput, "_", true);
        }

        if (pInput.contains("-")) {
            BaseLogger.log("Input detected as kebab-case.");
            return convertUsingDelimiter(pInput, "-", true);
        }

        BaseLogger.log("Input case is not recognized.");
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
            BaseLogger.log("Input for toPascalCase is null or empty.");
            return pInput;
        }

        if (!pInput.contains("_") && !pInput.contains("-") && Character.isLowerCase(pInput.charAt(0))) {
            BaseLogger.log("Input detected as camelCase.");
            return pInput.substring(0, 1).toUpperCase() + pInput.substring(1);
        }

        if (pInput.contains("_")) {
            BaseLogger.log("Input detected as snake_case.");
            return convertUsingDelimiter(pInput, "_", false);
        }

        if (pInput.contains("-")) {
            BaseLogger.log("Input detected as kebab-case.");
            return convertUsingDelimiter(pInput, "-", false);
        }

        BaseLogger.log("Input case is not recognized.");
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
            BaseLogger.log("Input for toSnakeCase is null or empty.");
            return pInput;
        }

        String result = pInput.replaceAll("([a-z])([A-Z])", "$1_$2");

        result = result.toLowerCase();

        result = result.replace("-", "_");

        BaseLogger.log("Converted to snake_case: " + result);
        return result;
    }

    /**
     * A {@link String} that converts a given {@link String} to kebab-case.
     * <p>
     * It converts camelCase, PascalCase, and snake_case to kebab-case by adding hyphens where appropriate.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The kebab-case version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String toKebabCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.log("Input for toKebabCase is null or empty.");
            return pInput;
        }

        String result = pInput.replaceAll("([a-z])([A-Z])", "$1-$2");

        result = result.toLowerCase();

        result = result.replace("_", "-");

        BaseLogger.log("Converted to kebab-case: " + result);
        return result;
    }

    /**
     * A {@link String} helper method to convert a string by splitting it using a given delimiter, then converting each part.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @param pDelimiter {@link String} - The delimiter used for splitting the input string.
     * @param pIsCamelCase {@code boolean} - Whether the result should be in camelCase (lowercase first letter).
     * @return The converted string.
     * @author MeAlam
     * @since 1.0.0
     */
    private static String convertUsingDelimiter(String pInput, String pDelimiter, boolean pIsCamelCase) {
        String[] parts = pInput.split(pDelimiter);
        StringBuilder convertedString = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0 && pIsCamelCase) {
                convertedString.append(parts[i].toLowerCase());
            } else {
                convertedString.append(toProperCase(parts[i]));
            }
        }
        String result = convertedString.toString();
        BaseLogger.log("Converted: " + result);
        return result;
    }

    /**
     * A {@link String} that converts a string to ProperCase, where the first letter is capitalized and the rest are lowercase.
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return The ProperCase version of the input string.
     * @author MeAlam
     * @since 1.0.0
     */
    private static String toProperCase(String pInput) {
        return pInput.substring(0, 1).toUpperCase() + pInput.substring(1).toLowerCase();
    }
}
