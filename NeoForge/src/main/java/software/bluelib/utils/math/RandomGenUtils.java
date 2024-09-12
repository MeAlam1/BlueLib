package software.bluelib.utils.math;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            throw new IllegalArgumentException("Minimum value must not be greater than maximum value.");
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
            throw new IllegalArgumentException("Minimum value must not be greater than maximum value.");
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
            throw new IllegalArgumentException("Length must be non-negative.");
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
            throw new IllegalArgumentException("Length must be non-negative.");
        }
        return pPrefix + generateRandomString(pLength - pPrefix.length());
    }

    /**
     * A {@code Date} that converts a string to a {@link Date} object.
     *
     * @param pDateStr {@link String} - The date in string format (e.g., "yyyy-MM-dd").
     * @param pFormat {@link String} - The format of the input date string.
     * @return The corresponding {@code Date} object.
     * @throws ParseException if the string cannot be parsed.
     * @author MeAlam
     * @since 1.0.0
     */
    public static Date stringToDate(String pDateStr, String pFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
        return formatter.parse(pDateStr);
    }

    /**
     * A {@code String} that converts a {@link Date} object to a string in a specified format.
     *
     * @param pDate {@link Date} - The date to be converted.
     * @param pFormat {@link String} - The desired date format (e.g., "yyyy-MM-dd").
     * @return The date as a string in the specified format.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String dateToString(Date pDate, String pFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
        return formatter.format(pDate);
    }


}
