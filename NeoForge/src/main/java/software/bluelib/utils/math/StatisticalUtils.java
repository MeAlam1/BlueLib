package software.bluelib.utils.math;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StatisticalUtils {

    /**
     * A {@code double} that calculates the mean (average) of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The mean of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMean(double[] pValues) {
        double sum = 0;
        for (double value : pValues) {
            sum += value;
        }
        return sum / pValues.length;
    }

    /**
     * A {@code double} that calculates the median of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The median of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMedian(double[] pValues) {
        double[] sorted = pValues.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;
        if (sorted.length % 2 == 0) {
            return (sorted[middle - 1] + sorted[middle]) / 2.0;
        } else {
            return sorted[middle];
        }
    }

    /**
     * A {@code double} that calculates the mode (most frequent value) of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The mode of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateMode(double[] pValues) {
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
     * A {@code double} that calculates the standard deviation of an array of values.
     *
     * @param pValues {@link double[]} - The array of values.
     * @return The standard deviation of the values.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateStandardDeviation(double[] pValues) {
        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumSquaredDifferences / pValues.length);
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
        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return sumSquaredDifferences / pValues.length;
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
        double max = Arrays.stream(pValues).max().orElseThrow();
        double min = Arrays.stream(pValues).min().orElseThrow();
        return max - min;
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
        double mean = calculateMean(pValues);
        double stdDev = calculateStandardDeviation(pValues);
        return (stdDev / mean) * 100;
    }





}
