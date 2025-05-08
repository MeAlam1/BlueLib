// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.math;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class StatisticalUtils {

    private StatisticalUtils() {}

    public static double calculateMean(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "mean"), true);
            return 0;
        }

        double sum = 0;
        for (double value : pValues) {
            sum += value;
        }
        return sum / pValues.length;
    }

    public static double calculateMedian(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "median"), true);
            return 0;
        }

        double[] sorted = pValues.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;

        return (sorted.length % 2 == 0) ? (sorted[middle - 1] + sorted[middle]) / 2.0 : sorted[middle];
    }

    public static double calculateMode(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "mode"), true);
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

    public static double calculateStandardDeviation(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "standard deviation"), true);
            return 0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumSquaredDifferences / pValues.length);
    }

    public static double calculateVariance(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "variance"), true);
            return 0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return sumSquaredDifferences / pValues.length;
    }

    public static double calculateRange(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "range"), true);
            return 0;
        }

        double max = Arrays.stream(pValues).max().orElseThrow();
        double min = Arrays.stream(pValues).min().orElseThrow();
        return max - min;
    }

    public static double calculateCoefficientOfVariation(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.null", "array", "coefficient of variation"), true);
            return 0;
        }

        double mean = calculateMean(pValues);
        double stdDev = calculateStandardDeviation(pValues);
        return (stdDev / mean) * 100;
    }
}
