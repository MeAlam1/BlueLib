// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.conversion;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A {@code class} providing methods for common unit and date conversions.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #inchesToCentimeters(double)} - Converts inches to centimeters.</li>
 *   <li>{@link #centimetersToInches(double)} - Converts centimeters to inches.</li>
 *   <li>{@link #celsiusToFahrenheit(double)} - Converts Celsius to Fahrenheit.</li>
 *   <li>{@link #fahrenheitToCelsius(double)} - Converts Fahrenheit to Celsius.</li>
 *   <li>{@link #kilometersToMiles(double)} - Converts kilometers to miles.</li>
 *   <li>{@link #milesToKilometers(double)} - Converts miles to kilometers.</li>
 *   <li>{@link #stringToDate(String, String)} - Converts a string to a {@link Date} object.</li>
 *   <li>{@link #dateToString(Date, String)} - Converts a {@link Date} object to a string.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class MathConverterUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private MathConverterUtils() {
    }

    /**
     * A {@link Double} that converts a length from inches to centimeters.
     *
     * @param pInches {@link Double} - The length in inches.
     * @return The length in centimeters.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double inchesToCentimeters(double pInches) {
        return pInches * 2.54;
    }

    /**
     * A {@link Double} that converts a length from centimeters to inches.
     *
     * @param pCentimeters {@link Double} - The length in centimeters.
     * @return The length in inches.
     * @since 1.0.0
     */
    public static double centimetersToInches(double pCentimeters) {
        return pCentimeters / 2.54;
    }

    /**
     * A {@link Double} that converts a temperature from Celsius to Fahrenheit.
     *
     * @param pCelsius {@link Double} - The temperature in Celsius.
     * @return The temperature in Fahrenheit.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double celsiusToFahrenheit(double pCelsius) {
        return pCelsius * 9 / 5 + 32;
    }

    /**
     * A {@link Double} that converts a temperature from Fahrenheit to Celsius.
     *
     * @param pFahrenheit {@link Double} - The temperature in Fahrenheit.
     * @return The temperature in Celsius.
     * @since 1.0.0
     */
    public static double fahrenheitToCelsius(double pFahrenheit) {
        return (pFahrenheit - 32) * 5 / 9;
    }

    /**
     * A {@link Double} that converts a distance from kilometers to miles.
     *
     * @param pKilometers {@link Double} - The distance in kilometers.
     * @return The distance in miles.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double kilometersToMiles(double pKilometers) {
        return pKilometers * 0.621371;
    }

    /**
     * A {@link Double} that converts a distance from miles to kilometers.
     *
     * @param pMiles {@link Double} - The distance in miles.
     * @return The distance in kilometers.
     * @since 1.0.0
     */
    public static double milesToKilometers(double pMiles) {
        return pMiles / 0.621371;
    }

    /**
     * A {@link Date} that converts a string to a {@link Date} object.
     *
     * @param pDateStr {@link String} - The date in string format (e.g., "yyyy-MM-dd").
     * @param pFormat  {@link String} - The format of the input date string.
     * @return The corresponding {@code Date} object.
     * @throws ParseException if the string cannot be parsed.
     * @author MeAlam
     * @since 1.0.0
     */
    public static Date stringToDate(String pDateStr, String pFormat) throws ParseException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
            return formatter.parse(pDateStr);
        } catch (ParseException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error parsing date string: " + pDateStr + " with format: " + pFormat, pException, true);
            throw pException;
        }
    }

    /**
     * A {@link String} that converts a {@link Date} object to a string in a specified format.
     *
     * @param pDate   {@link Date} - The date to be converted.
     * @param pFormat {@link String} - The desired date format (e.g., "yyyy-MM-dd").
     * @return The date as a string in the specified format.
     * @author MeAlam
     * @since 1.0.0
     */
    public static String dateToString(Date pDate, String pFormat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
            return formatter.format(pDate);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error formatting date: " + pDate.toString() + " with format: " + pFormat, pException, true);
            return pException.getMessage();
        }
    }
}
