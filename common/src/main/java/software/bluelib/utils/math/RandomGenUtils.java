// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code class} for generating random values of various types.
 * <p>
 * This class provides static methods to generate random integers, doubles, booleans, and alphanumeric strings.
 * The methods offer flexibility in specifying the range or length for the generated values, and they ensure
 * proper logging and error handling if invalid parameters are provided.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #generateRandomInt(int, int)} - Generates a random integer between a specified minimum and maximum value (inclusive).</li>
 * <li>{@link #generateRandomDouble(double, double)} - Generates a random double between a specified minimum and maximum value (inclusive).</li>
 * <li>{@link #generateRandomBoolean()} - Generates a random boolean value.</li>
 * <li>{@link #generateRandomString(int)} - Generates a random alphanumeric string of a specified length.</li>
 * <li>{@link #generateRandomStringWithPrefix(String, int)} - Generates a random alphanumeric string with a specified prefix and length.</li>
 * </ul>
 * <p>
 * Each method logs errors using {@link BaseLogger} when invalid parameters are provided (e.g., negative lengths
 * or minimum values greater than maximum values) and returns default values (e.g., `0` for integers and `"unknown"` for strings)
 * in such cases.
 * </p>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class RandomGenUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private RandomGenUtils() {}

    /**
     * A {@link Integer} that generates a random integer between a specified minimum and maximum value (inclusive).
     *
     * @param pMin {@link Integer} - The minimum value (inclusive).
     * @param pMax {@link Integer} - The maximum value (inclusive).
     * @return A random integer between {@code pMin} and {@code pMax}.
     * @author MeAlam
     * @since 1.0.0
     */
    public static int generateRandomInt(int pMin, int pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.log(BaseLogLevel.WARNING, "Error generating random integer", throwable, true);
            return 0;
        }
        return pMin + (int) (Math.random() * (pMax - pMin + 1));
    }

    /**
     * A {@link Double} that generates a random double between a specified minimum and maximum value (inclusive).
     *
     * @param pMin {@link Double} - The minimum value (inclusive).
     * @param pMax {@link Double} - The maximum value (inclusive).
     * @return A random double between {@code pMin} and {@code pMax}.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double generateRandomDouble(double pMin, double pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.log(BaseLogLevel.WARNING, "Error generating random double", true);
            return 0;
        }
        return pMin + Math.random() * (pMax - pMin);
    }

    /**
     * A {@link Boolean} that generates a random boolean value.
     *
     * @return A random boolean value.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean generateRandomBoolean() {
        return Math.random() < 0.5;
    }

    /**
     * A {@link String} that generates a random alphanumeric string of a specified length.
     *
     * @param pLength {@link Integer} - The length of the string to be generated.
     * @return A random alphanumeric string of the specified length.
     * @throws IllegalArgumentException if {@code pLength} is negative.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String generateRandomString(int pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.log(BaseLogLevel.WARNING, "Error generating random string", throwable, true);
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
     * A {@link String} that generates a random alphanumeric string of a specified length with a specified prefix.
     *
     * @param pPrefix {@link String} - The prefix of the string.
     * @param pLength {@link Integer} - The length of the string to be generated.
     * @return A random alphanumeric string of the specified length with the specified prefix.
     * @throws IllegalArgumentException if {@code pLength} is negative.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String generateRandomStringWithPrefix(String pPrefix, int pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.log(BaseLogLevel.WARNING, "Error generating random string with prefix", throwable, true);
            return "unknown";
        }
        return pPrefix + generateRandomString(pLength - pPrefix.length());
    }
}
