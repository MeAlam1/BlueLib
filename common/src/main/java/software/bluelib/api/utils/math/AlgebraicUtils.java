/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.Translation;

@SuppressWarnings("unused")
public class AlgebraicUtils {

    private AlgebraicUtils() {}

    public static double[] solveQuadraticEquation(double pA, double pB, double pC) {
        double discriminant = pB * pB - 4 * pA * pC;
        if (discriminant < 0) {
            Throwable throwable = new IllegalArgumentException("Number must be non-negative.");
            BaseLogger.log(true, BaseLogLevel.WARNING, Translation.log("math.error.calc", "quadratic"), throwable);
            return new double[0];
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root1 = (-pB + sqrtDiscriminant) / (2 * pA);
        double root2 = (-pB - sqrtDiscriminant) / (2 * pA);

        return new double[] { root1, root2 };
    }

    public static long factorial(int pNumber) {
        if (pNumber < 0) {
            Throwable throwable = new IllegalArgumentException("Number must be non-negative.");
            BaseLogger.log(true, BaseLogLevel.WARNING, Translation.log("math.error.calc", "factorial"), throwable);
            return 0;
        }

        long result = 1;
        for (int i = 1; i <= pNumber; i++) {
            result *= i;
        }

        return result;
    }

    public static int calculateGCD(int pA, int pB) {
        while (pB != 0) {
            int temp = pB;
            pB = pA % pB;
            pA = temp;
        }

        return pA;
    }

    public static <T> List<Set<T>> generatePowerSet(Set<T> pSet) {
        List<Set<T>> powerSet = new ArrayList<>();
        powerSet.add(new HashSet<>());
        for (T element : pSet) {
            List<Set<T>> newSubsets = new ArrayList<>();
            for (Set<T> subset : powerSet) {
                Set<T> newSubset = new HashSet<>(subset);
                newSubset.add(element);
                newSubsets.add(newSubset);
            }
            powerSet.addAll(newSubsets);
        }

        return powerSet;
    }
}
