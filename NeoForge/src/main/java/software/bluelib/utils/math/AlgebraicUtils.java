package software.bluelib.utils.math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgebraicUtils {

    /**
     * A {@code double[]} that solves the quadratic equation {@code ax^2 + bx + c = 0}
     * using the quadratic formula.
     *
     * @param pA {@link double} - The coefficient a of the quadratic equation.
     * @param pB {@link double} - The coefficient b of the quadratic equation.
     * @param pC {@link double} - The coefficient c of the quadratic equation.
     * @return An array of roots of the quadratic equation. The array may be empty if there are no real roots.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double[] solveQuadraticEquation(double pA, double pB, double pC) {
        double discriminant = pB * pB - 4 * pA * pC;
        if (discriminant < 0) {
            return new double[0];
        }
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root1 = (-pB + sqrtDiscriminant) / (2 * pA);
        double root2 = (-pB - sqrtDiscriminant) / (2 * pA);
        return new double[] { root1, root2 };
    }

    /**
     * A {@code long} that calculates the factorial of a non-negative integer.
     *
     * @param pNumber {@link int} - The non-negative integer.
     * @return The factorial of the integer.
     * @throws IllegalArgumentException if {@code pNumber} is negative.
     * @author MeAlam
     * @since 1.0.0
     */
    public static long factorial(int pNumber) {
        if (pNumber < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        long result = 1;
        for (int i = 1; i <= pNumber; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * A {@code int} that calculates the greatest common divisor of two integers using the Euclidean algorithm.
     *
     * @param pA {@link int} - The first integer.
     * @param pB {@link int} - The second integer.
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
     * A {@code List<Set<T>>} that generates the power set (all subsets) of a given set.
     *
     * @param pSet {@link Set<T>} - The input set.
     * @param <T> The type of elements in the set.
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
