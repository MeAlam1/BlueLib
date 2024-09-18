// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogger;

public class RandomGenUtils {

    /**
     * A {@code int} that generates a random integer between a specified minimum and maximum value (inclusive).
     *
     * @param pMin {@link int} - The minimum value (inclusive).
     * @param pMax {@link int} - The maximum value (inclusive).
     * @return A random integer between {@code pMin} and {@code pMax}.
     * @author MeAlam
     * @since 1.0.0
     */
    public static int generateRandomInt(int pMin, int pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.logError("Error generating random integer", throwable);
            return 0;
        }
        return pMin + (int)(Math.random() * (pMax - pMin + 1));
    }

    /**
     * A {@code double} that generates a random double between a specified minimum and maximum value (inclusive).
     *
     * @param pMin {@link double} - The minimum value (inclusive).
     * @param pMax {@link double} - The maximum value (inclusive).
     * @return A random double between {@code pMin} and {@code pMax}.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double generateRandomDouble(double pMin, double pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.logError("Error generating random double", throwable);
            return 0;
        }
        return pMin + Math.random() * (pMax - pMin);
    }

    /**
     * A {@code boolean} that generates a random boolean value.
     *
     * @return A random boolean value.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean generateRandomBoolean() {
        return Math.random() < 0.5;
    }

    /**
     * A {@code String} that generates a random alphanumeric string of a specified length.
     *
     * @param pLength {@link int} - The length of the string to be generated.
     * @return A random alphanumeric string of the specified length.
     * @throws IllegalArgumentException if {@code pLength} is negative.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String generateRandomString(int pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.logError("Error generating random string", throwable);
            return "unknown";
        }
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(pLength);
        for (int i = 0; i < pLength; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    /**
     * A {@code String} that generates a random alphanumeric string of a specified length with a specified prefix.
     *
     * @param pPrefix {@link String} - The prefix of the string.
     * @param pLength {@link int} - The length of the string to be generated.
     * @return A random alphanumeric string of the specified length with the specified prefix.
     * @throws IllegalArgumentException if {@code pLength} is negative.
     * @since 1.0.0
     */
    public static String generateRandomStringWithPrefix(String pPrefix, int pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.logError("Error generating random string with prefix", throwable);
            return "unknown";
        }
        return pPrefix + generateRandomString(pLength - pPrefix.length());
    }
}
