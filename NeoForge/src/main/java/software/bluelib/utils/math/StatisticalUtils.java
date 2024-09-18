// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StatisticalUtils {

    /**
     * A {@link Double} that calculates the mean (average) of an array of values.
     *
     * @param pValues {@link Double}{@code []} - The array of values.
     * @return The mean of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMean(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, mean calculation might fail.");
            return 0;
        }

        double sum = 0;
        for (double value : pValues) {
            sum += value;
        }
        double mean = sum / pValues.length;
        BaseLogger.bluelibLogSuccess("Mean successfully calculated: " + mean);
        return mean;
    }

    /**
     * A {@link Double} that calculates the median of an array of values.
     *
     * @param pValues {@link Double}{@code []} - The array of values.
     * @return The median of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMedian(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, median calculation might fail.");
            return 0;
        }

        double[] sorted = pValues.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;
        double median = (sorted.length % 2 == 0) ?
                (sorted[middle - 1] + sorted[middle]) / 2.0 :
                sorted[middle];

        BaseLogger.bluelibLogSuccess("Median successfully calculated: " + median);
        return median;
    }

    /**
     * A {@code double} that calculates the mode (the most frequent value) of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The mode of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMode(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, mode calculation might fail.");
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

        BaseLogger.bluelibLogSuccess("Mode successfully calculated: " + mode);
        return mode;
    }

    /**
     * A {@code double} that calculates the standard deviation of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The standard deviation of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateStandardDeviation(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, standard deviation calculation might fail.");
            return 0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        double stdDev = Math.sqrt(sumSquaredDifferences / pValues.length);
        BaseLogger.bluelibLogSuccess("Standard deviation successfully calculated: " + stdDev);
        return stdDev;
    }

    /**
     * A {@code double} that calculates the variance of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The variance of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateVariance(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, variance calculation might fail.");
            return 0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        double variance = sumSquaredDifferences / pValues.length;
        BaseLogger.bluelibLogSuccess("Variance successfully calculated: " + variance);
        return variance;
    }

    /**
     * A {@code double} that calculates the range (difference between maximum and minimum) of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The range of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateRange(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, range calculation might fail.");
            return 0;
        }

        double max = Arrays.stream(pValues).max().orElseThrow();
        double min = Arrays.stream(pValues).min().orElseThrow();
        double range = max - min;
        BaseLogger.bluelibLogSuccess("Range successfully calculated: " + range);
        return range;
    }

    /**
     * A {@code double} that calculates the coefficient of variation of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The coefficient of variation of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateCoefficientOfVariation(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.logWarning("Array is empty, coefficient of variation calculation might fail.");
            return 0;
        }

        double mean = calculateMean(pValues);
        double stdDev = calculateStandardDeviation(pValues);
        double coefficient = (stdDev / mean) * 100;
        BaseLogger.bluelibLogSuccess("Coefficient of variation successfully calculated: " + coefficient);
        return coefficient;
    }
}
