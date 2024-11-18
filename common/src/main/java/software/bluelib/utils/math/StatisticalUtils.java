// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code class} for performing various statistical calculations on arrays of double values.
 * <p>
 * This class includes methods to compute key statistical metrics such as mean, median, mode, standard deviation,
 * variance, range, and coefficient of variation. Each method logs appropriate messages for success or warnings
 * when the input array is empty.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #calculateMean(double[])} - Calculates the mean (average) of an array of values.</li>
 *   <li>{@link #calculateMedian(double[])} - Calculates the median value of an array of values.</li>
 *   <li>{@link #calculateMode(double[])} - Determines the mode (most frequent value) of an array of values.</li>
 *   <li>{@link #calculateStandardDeviation(double[])} - Computes the standard deviation of an array of values.</li>
 *   <li>{@link #calculateVariance(double[])} - Computes the variance of an array of values.</li>
 *   <li>{@link #calculateRange(double[])} - Determines the range (difference between maximum and minimum) of an array of values.</li>
 *   <li>{@link #calculateCoefficientOfVariation(double[])} - Calculates the coefficient of variation (CV) of an array of values.</li>
 * </ul>
 * <p>
 * Each method logs a success message with the computed value or a warning if the input array is empty.
 * The logging is done via {@link BaseLogger}, ensuring that any issues or results are recorded appropriately.
 * </p>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class StatisticalUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private StatisticalUtils() {
    }

    /**
     * A {@link Double} that calculates the mean (average) of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated mean.
     * </p>
     *
     * @param pValues {@link double[]} - The array of values to calculate the mean for.
     * @return The mean of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMean(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, mean calculation might fail.", true);
            return 0;
        }

        double sum = 0;
        for (double value : pValues) {
            sum += value;
        }
        return sum / pValues.length;
    }

    /**
     * A {@link Double}{@code []} that calculates the median of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated median.
     * </p>
     *
     * @param pValues {@link Double}{@code []} - The array of values to calculate the median for.
     * @return The median of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMedian(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, median calculation might fail.", true);
            return 0;
        }

        double[] sorted = pValues.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;

        return (sorted.length % 2 == 0) ?
                (sorted[middle - 1] + sorted[middle]) / 2.0 :
                sorted[middle];
    }

    /**
     * A {@link Double}{@code []} that calculates the mode (the most frequent value) of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated mode.
     * </p>
     *
     * @param pValues {@link Double}{@code []} - The array of values to calculate the mode for.
     * @return The mode of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMode(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, mode calculation might fail.", true);
            return 0;
        }

        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (double value : pValues) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }

        double mode = pValues[0];
        int maxCount = 0;
        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }
        return mode;
    }

    /**
     * A {@link Double}{@code []} that calculates the standard deviation of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated standard deviation.
     * </p>
     *
     * @param pValues {@link Double}{@code []} - The array of values to calculate the standard deviation for.
     * @return The standard deviation of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateStandardDeviation(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, standard deviation calculation might fail.", true);
            return 0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumSquaredDifferences / pValues.length);
    }

    /**
     * A {@link Double}{@code []} that calculates the variance of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated variance.
     * </p>
     *
     * @param pValues {@link Double}{@code []} - The array of values to calculate the variance for.
     * @return The variance of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateVariance(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, variance calculation might fail.", true);
            return 0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return sumSquaredDifferences / pValues.length;
    }

    /**
     * A {@link Double}{@code []} that calculates the range (difference between maximum and minimum) of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated range.
     * </p>
     *
     * @param pValues {@link Double}{@code []} - The array of values to calculate the range for.
     * @return The range of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateRange(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, range calculation might fail.", true);
            return 0;
        }

        double max = Arrays.stream(pValues).max().orElseThrow();
        double min = Arrays.stream(pValues).min().orElseThrow();
        return max - min;
    }

    /**
     * A {@link Double}{@code []} that calculates the coefficient of variation of an array of values.
     * <p>
     * Logs a warning if the array is empty and a success message with the calculated coefficient of variation.
     * </p>
     *
     * @param pValues {@link Double}{@code []} - The array of values to calculate the coefficient of variation for.
     * @return The coefficient of variation of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateCoefficientOfVariation(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "Array is empty, coefficient of variation calculation might fail.", true);
            return 0;
        }

        double mean = calculateMean(pValues);
        double stdDev = calculateStandardDeviation(pValues);
        return (stdDev / mean) * 100;
    }
}
