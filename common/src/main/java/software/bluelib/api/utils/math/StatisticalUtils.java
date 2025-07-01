/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.math;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class StatisticalUtils {

    private StatisticalUtils() {}

    public static @NotNull Double calculateMean(@NotNull Double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "mean"));
            return 0.0;
        }

        double sum = 0;
        for (double value : pValues) {
            sum += value;
        }
        return sum / pValues.length;
    }

    public static @NotNull Double calculateMedian(@NotNull Double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "median"));
            return 0.0;
        }

        Double[] sorted = pValues.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;

        return (sorted.length % 2 == 0) ? (sorted[middle - 1] + sorted[middle]) / 2.0 : sorted[middle];
    }

    public static @NotNull Double calculateMode(@NotNull Double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "mode"));
            return 0.0;
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

    public static @NotNull Double calculateStandardDeviation(@NotNull Double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "standard deviation"));
            return 0.0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (@NotNull
        Double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumSquaredDifferences / pValues.length);
    }

    public static @NotNull Double calculateVariance(@NotNull Double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "variance"));
            return 0.0;
        }

        double mean = calculateMean(pValues);
        double sumSquaredDifferences = 0;
        for (double value : pValues) {
            sumSquaredDifferences += Math.pow(value - mean, 2);
        }
        return sumSquaredDifferences / pValues.length;
    }

    public static @NotNull Double calculateRange(double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "range"));
            return 0.0;
        }

        double max = Arrays.stream(pValues).max().orElseThrow();
        double min = Arrays.stream(pValues).min().orElseThrow();
        return max - min;
    }

    public static @NotNull Double calculateCoefficientOfVariation(@NotNull Double[] pValues) {
        if (pValues.length == 0) {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.null", "array", "coefficient of variation"));
            return 0.0;
        }

        @NotNull
        Double mean = calculateMean(pValues);
        @NotNull
        Double stdDev = calculateStandardDeviation(pValues);
        return (stdDev / mean) * 100;
    }
}
