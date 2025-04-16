// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class AlgebraicUtils {

    private AlgebraicUtils() {}

    public static double[] solveQuadraticEquation(double pA, double pB, double pC) {
        double discriminant = pB * pB - 4 * pA * pC;
        if (discriminant < 0) {
            BaseLogger.log(BaseLogLevel.WARNING, "No real roots found for the quadratic equation.", true);
            return new double[0];
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root1 = (-pB + sqrtDiscriminant) / (2 * pA);
        double root2 = (-pB - sqrtDiscriminant) / (2 * pA);

        BaseLogger.log(BaseLogLevel.INFO, "Roots found: root1=" + root1 + ", root2=" + root2, true);
        return new double[] { root1, root2 };
    }

    public static long factorial(int pNumber) {
        if (pNumber < 0) {
            IllegalArgumentException exception = new IllegalArgumentException("Number must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Attempted to calculate factorial of a negative number: " + pNumber, exception, true);
            throw exception;
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
