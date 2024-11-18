// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@code class} providing methods for various algebraic and combinatorial calculations.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #solveQuadraticEquation(double, double, double)} - Solves a quadratic equation.</li>
 *   <li>{@link #factorial(int)} - Calculates the factorial of a non-negative integer.</li>
 *   <li>{@link #calculateGCD(int, int)} - Calculates the greatest common divisor of two integers.</li>
 *   <li>{@link #generatePowerSet(Set)} - Generates the power set of a given set.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class AlgebraicUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private AlgebraicUtils() {
    }

    /**
     * A {@link Double}{@code []} that solves the quadratic equation {@code ax^2 + bx + c = 0}
     * using the quadratic formula.
     *
     * @param pA {@link Double} - The coefficient a of the quadratic equation.
     * @param pB {@link Double} - The coefficient b of the quadratic equation.
     * @param pC {@link Double} - The coefficient c of the quadratic equation.
     * @return An array of roots of the quadratic equation. The array may be empty if there are no real roots.
     * @author MeAlam
     * @since 1.0.0
     */
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
        return new double[]{root1, root2};
    }

    /**
     * A {@link Long} that calculates the factorial of a non-negative integer.
     *
     * @param pNumber {@link Integer} - The non-negative integer.
     * @return The factorial of the integer.
     * @throws IllegalArgumentException if {@code pNumber} is negative.
     * @author MeAlam
     * @since 1.0.0
     */
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

    /**
     * A {@link Integer} that calculates the greatest common divisor of two integers using the Euclidean algorithm.
     *
     * @param pA {@link Integer} - The first integer.
     * @param pB {@link Integer} - The second integer.
     * @return The greatest common divisor of {@code pA} and {@code pB}.
     * @author MeAlam
     * @since 1.0.0
     */
    public static int calculateGCD(int pA, int pB) {

        while (pB != 0) {
            int temp = pB;
            pB = pA % pB;
            pA = temp;
        }

        return pA;
    }

    /**
     * A {@link List<Set>} that generates the power set (all subsets) of a given set.
     *
     * @param pSet {@link Set<T>} - The input set.
     * @param <T>  The type of elements in the set.
     * @return A list of all subsets of the input set.
     * @author MeAlam
     * @since 1.0.0
     */
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
