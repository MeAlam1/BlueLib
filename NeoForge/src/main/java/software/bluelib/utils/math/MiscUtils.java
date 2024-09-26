// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code class} providing utility methods for various common operations.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #isValidEmail(String)} - Checks if a string is a valid email address.</li>
 *   <li>{@link #stringToIntWithDefault(String, int)} - Converts a string to an integer with a default value if conversion fails.</li>
 *   <li>{@link #calculateLevenshteinDistance(String, String)} - Calculates the Levenshtein distance between two strings.</li>
 *   <li>{@link #hexToRGB(String)} - Converts a hexadecimal color code to an RGB array.</li>
 * </ul>
 * @author MeAlam
 * @since 1.0.0
 */
public class MiscUtils {

    /**
     * A {@link Boolean} that checks if a string is a valid email address.
     *
     * @param pEmail {@link String} - The string to be checked.
     * @return {@code true} if the string is a valid email address, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean isValidEmail(String pEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return pEmail != null && pEmail.matches(emailRegex);
    }

    /**
     * A {@link Integer} that converts a string to an integer, returning a default value if the string is not a valid integer.
     *
     * @param pString {@link String} - The string to be converted.
     * @param pDefaultValue {@link Integer} - The default value to return if the string is not a valid integer.
     * @return The integer value of the string, or {@code pDefaultValue} if the string is not a valid integer.
     * @author MeAlam
     * @since 1.0.0
     */
    public static int stringToIntWithDefault(String pString, int pDefaultValue) {
        try {
            return Integer.parseInt(pString);
        } catch (NumberFormatException pException) {
            BaseLogger.log("Error converting string to integer", pException);
            return pDefaultValue;
        }
    }

    /**
     * A {@link Integer} that calculates the Levenshtein distance between two strings.
     *
     * @param pStr1 {@link String} - The first string.
     * @param pStr2 {@link String} - The second string.
     * @return The Levenshtein distance between the two strings.
     * @author MeAlam
     * @since 1.0.0
     */
    public static int calculateLevenshteinDistance(String pStr1, String pStr2) {
        int[][] dp = new int[pStr1.length() + 1][pStr2.length() + 1];
        for (int i = 0; i <= pStr1.length(); i++) {
            for (int j = 0; j <= pStr2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (pStr1.charAt(i - 1) == pStr2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                    );
                }
            }
        }
        return dp[pStr1.length()][pStr2.length()];
    }

    /**
     * A {@link Integer}{@code []} that converts a hexadecimal color code to an RGB array.
     *
     * @param pHex {@link String} - The hexadecimal color code (e.g., "#FFFFFF").
     * @return An array containing the RGB values.
     * @throws IllegalArgumentException if the hexadecimal color code is invalid.
     * @author MeAlam
     * @since 1.0.0
     */
    public static int[] hexToRGB(String pHex) {
        if (pHex == null || pHex.isEmpty()) {
            Throwable throwable = new IllegalArgumentException("Hex color code cannot be null or empty.");
            BaseLogger.log("Error converting hex to RGB", throwable);
            return new int[]{0, 0, 0};
        }
        if (pHex.charAt(0) == '#') {
            pHex = pHex.substring(1);
        }
        if (pHex.length() != 6) {
            Throwable throwable = new IllegalArgumentException("Invalid hex color code.");
            BaseLogger.log("Error converting hex to RGB", throwable);
            return new int[]{0, 0, 0};
        }
        try {
            int r = Integer.parseInt(pHex.substring(0, 2), 16);
            int g = Integer.parseInt(pHex.substring(2, 4), 16);
            int b = Integer.parseInt(pHex.substring(4, 6), 16);
            return new int[]{r, g, b};
        } catch (NumberFormatException pException) {
            BaseLogger.log("Error parsing hex color code to RGB", pException);
            return new int[]{0, 0, 0};
        }
    }
}
