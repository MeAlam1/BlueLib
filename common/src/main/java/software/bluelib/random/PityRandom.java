/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.jetbrains.annotations.ApiStatus;


@SuppressWarnings("unused")
@ApiStatus.Internal
public class PityRandom {

    private final int minInt;
    private final int maxInt;
    private final double minDouble;
    private final double maxDouble;
    private final Map<Integer, Integer> selectionCountInt;
    private final Map<Double, Integer> selectionCountDouble;
    private final Random random;

    public PityRandom(int pMin, int pMax) {
        this.minInt = pMin;
        this.maxInt = pMax;
        this.minDouble = pMin;
        this.maxDouble = pMax;
        this.selectionCountInt = new HashMap<>();
        this.selectionCountDouble = new HashMap<>();
        this.random = new Random();

        for (int i = pMin; i <= pMax; i++) {
            selectionCountInt.put(i, 0);
        }
    }

    public int nextInt() {
        Map<Integer, Double> weights = new HashMap<>();
        int maxCount = selectionCountInt.values().stream().max(Integer::compareTo).orElse(1);

        for (int num = minInt; num <= maxInt; num++) {
            int count = selectionCountInt.get(num);
            weights.put(num, Math.pow(2, maxCount - count));
        }

        double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        TreeMap<Double, Integer> probabilityMap = new TreeMap<>();
        double cumulative = 0.0;

        for (Map.Entry<Integer, Double> entry : weights.entrySet()) {
            cumulative += entry.getValue() / totalWeight;
            probabilityMap.put(cumulative, entry.getKey());
        }

        double roll = random.nextDouble();
        int selected = probabilityMap.ceilingEntry(roll).getValue();
        selectionCountInt.put(selected, selectionCountInt.get(selected) + 1);
        return selected;
    }

    public boolean nextBoolean() {
        int trueCount = selectionCountInt.getOrDefault(1, 0);
        int falseCount = selectionCountInt.getOrDefault(0, 0);
        int maxCount = Math.max(trueCount, falseCount);

        double trueWeight = Math.pow(2, maxCount - trueCount);
        double falseWeight = Math.pow(2, maxCount - falseCount);
        double totalWeight = trueWeight + falseWeight;

        boolean selected = (random.nextDouble() < (trueWeight / totalWeight));
        selectionCountInt.put(selected ? 1 : 0, selectionCountInt.getOrDefault(selected ? 1 : 0, 0) + 1);
        return selected;
    }

    private double getNextValue(double min, double max, Map<Double, Integer> selectionCount) {
        Map<Double, Double> weights = new HashMap<>();
        double maxCount = selectionCount.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        for (double num = min; num <= max; num += 0.01) {
            int count = selectionCount.getOrDefault(num, 0);
            weights.put(num, Math.pow(2, maxCount - count));
        }

        double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        TreeMap<Double, Double> probabilityMap = new TreeMap<>();
        double cumulative = 0.0;

        for (Map.Entry<Double, Double> entry : weights.entrySet()) {
            cumulative += entry.getValue() / totalWeight;
            probabilityMap.put(cumulative, entry.getKey());
        }

        double roll = random.nextDouble();
        double selected = probabilityMap.ceilingEntry(roll).getValue();
        selectionCount.put(selected, selectionCount.getOrDefault(selected, 0) + 1);
        return selected;
    }

    public float nextFloat() {
        return (float) getNextValue(minDouble, maxDouble, selectionCountDouble);
    }

    public double nextDouble() {
        return getNextValue(minDouble, maxDouble, selectionCountDouble);
    }
}
