// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils;

import software.bluelib.utils.logging.BaseLogger;

/**
 * a {@code class} for converting strings between various cases, including camelCase, PascalCase, snake_case, and others.
 * <p>
 * This class provides static methods for case conversion to facilitate consistent string formatting throughout the application.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #toCamelCase(String)} - Converts a string to camelCase format.</li>
 *   <li>{@link #toPascalCase(String)} - Converts a string to PascalCase format.</li>
 *   <li>{@link #toProperCase(String)} - Converts a string to ProperCase format, capitalizing the first letter.</li>
 *   <li>{@link #toSnakeCase(String)} - Converts a string to snake_case format.</li>
 *   <li>{@link #toLowerCase(String)} - Converts a string to lowercase.</li>
 *   <li>{@link #toUpperCase(String)} - Converts a string to UPPERCASE.</li>
 * </ul>
 * </p>
 *
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 */
public class CaseConverterUtil {

    /**
     * A {@link String} that converts a string to camelCase format.
     * <p>
     * This method splits the input string by underscores and concatenates the parts, making the first letter of each subsequent word uppercase.
     * </p>
     *
     * @param pInput {@link String} - The input string to be converted. (Needs to be in snake_case format)
     * @return {@link String} - The converted camelCase string.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a string to PascalCase format.
     * <p>
     * This method splits the input string by underscores and concatenates the parts, making the first letter of each word uppercase.
     * </p>
     *
     * @param pInput {@link String} - The input string to be converted. (Needs to be in snake_case format)
     * @return {@link String} - The converted PascalCase string.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a string to ProperCase format.
     * <p>
     * ProperCase capitalizes the first letter of the input string and converts the rest to lowercase.
     * </p>
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return {@link String} - The converted ProperCase string.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public static String toProperCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toProperCase is null or empty.");
            return pInput;
        }
        String result = pInput.substring(0, 1).toUpperCase() + pInput.substring(1).toLowerCase();
        BaseLogger.bluelibLogSuccess("Converted to ProperCase: " + result);
        return result;
    }

    /**
     * A {@link String} that converts a string to snake_case format.
     * <p>
     * This method transforms uppercase letters into lowercase and inserts an underscore before each uppercase letter.
     * </p>
     *
     * @param pInput {@link String} - The input string to be converted. (Needs to be in camelCase or PascalCase format)
     * @return {@link String} - The converted snake_case string.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
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

    /**
     * A {@link String} that converts a string to lowercase.
     * <p>
     * This method converts all characters in the input string to lowercase.
     * </p>
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return {@link String} - The converted lowercase string.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public static String toLowerCase(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            BaseLogger.logWarning("Input for toLowerCase is null or empty.");
            return pInput;
        }
        String result = pInput.toLowerCase();
        BaseLogger.bluelibLogSuccess("Converted to lowercase: " + result);
        return result;
    }

    /**
     * A {@link String} that converts a string to UPPERCASE.
     * <p>
     * This method converts all characters in the input string to uppercase.
     * </p>
     *
     * @param pInput {@link String} - The input string to be converted.
     * @return {@link String} - The converted UPPERCASE string.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
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
