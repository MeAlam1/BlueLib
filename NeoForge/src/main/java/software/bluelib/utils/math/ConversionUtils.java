package software.bluelib.utils.math;

public class ConversionUtils {

    /**
     * A {@code double} that converts a length from inches to centimeters.
     *
     * @param pInches {@link double} - The length in inches.
     * @return The length in centimeters.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double inchesToCentimeters(double pInches) {
        return pInches * 2.54;
    }

    /**
     * A {@code double} that converts a length from centimeters to inches.
     *
     * @param pCentimeters {@link double} - The length in centimeters.
     * @return The length in inches.
     * @since 1.0.0
     */
    public static double centimetersToInches(double pCentimeters) {
        return pCentimeters / 2.54;
    }


    /**
     * A {@code double} that converts a temperature from Celsius to Fahrenheit.
     *
     * @param pCelsius {@link double} - The temperature in Celsius.
     * @return The temperature in Fahrenheit.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double celsiusToFahrenheit(double pCelsius) {
        return pCelsius * 9 / 5 + 32;
    }

    /**
     * A {@code double} that converts a temperature from Fahrenheit to Celsius.
     *
     * @param pFahrenheit {@link double} - The temperature in Fahrenheit.
     * @return The temperature in Celsius.
     * @since 1.0.0
     */
    public static double fahrenheitToCelsius(double pFahrenheit) {
        return (pFahrenheit - 32) * 5 / 9;
    }

    /**
     * A {@code double} that converts a distance from kilometers to miles.
     *
     * @param pKilometers {@link double} - The distance in kilometers.
     * @return The distance in miles.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double kilometersToMiles(double pKilometers) {
        return pKilometers * 0.621371;
    }


}
