package software.bluelib.random;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * <b>WARNING:</b> <i>Still a massive Work in Progress.</i> <br>
 * A randomizer with a pity system to balance probability.
 * <p>
 * The probability of selecting a specific value decreases slightly after it is picked.
 * The least selected value has a significantly higher chance of appearing.
 * <p>
 * Probability is adjusted using an inverse weighting system:
 * <ul>
 *     <li>The more a value has been picked, the lower its chance.</li>
 *     <li>The least picked value will always have the highest chance.</li>
 *     <li>Uses exponential scaling (2^(maxCount - count)) for smoother weight adjustment.</li>
 * </ul>
 * <p>
 * Supported types:
 * <ul>
 *     <li>Integer (default range-based selection)</li>
 *     <li>Boolean (weighted random true/false selection)</li>
 *     <li>Float and Double (randomized within a range, with pity weighting applied)</li>
 * </ul>
 * <p>
 * Probability Breakdown:
 * <ul>
 *     <li>If all values have been picked equally, they each have a 1/N chance.</li>
 *     <li>If one value has been picked more times than another, its weight is halved per additional selection.</li>
 *     <li>Example: If we have (1,2,3,4,5) and 3 has been picked twice while others once, then 3's weight is reduced by half.</li>
 * </ul>
 * <p>
 * Math formula for weighting:
 * <ul>
 *     <li>For each value <code>x</code>, its weight <code>w(x)</code> is calculated as:</li>
 *     <pre><code>w(x) = 2^(maxCount - count(x))</code></pre>
 *     <li>Where <code>maxCount</code> is the highest count of selections made for any value, and <code>count(x)</code> is the number of times <code>x</code> has been selected.</li>
 *     <li>The weight decreases exponentially for values with higher selection counts, and increases for values with fewer selections, encouraging the selection of less-picked values.</li>
 * </ul>
 * <p>
 * In other words:
 * <ul>
 *     <li>If <code>x</code> has been picked once, its weight is <code>2^(maxCount - 1)</code>.</li>
 *     <li>If <code>x</code> has been picked twice, its weight is <code>2^(maxCount - 2)</code>.</li>
 *     <li>This exponential decay ensures that values picked more frequently are less likely to be selected again, while those picked less frequently are more likely to be selected.</li>
 * </ul>
 */
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
