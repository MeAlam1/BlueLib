/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <b>WARNING:</b> <i>Still a massive Work in Progress.</i> <br>
 * A randomizer with a pity system to balance probability.
 * <p>
 * The probability of selecting a specific value decreases slightly after it is picked.
 * The least selected value has a significantly higher chance of appearing.
 * <p>
 * Probability is adjusted using an inverse weighting system:
 * <ul>
 * <li>The more a value has been picked, the lower its chance.</li>
 * <li>The least picked value will always have the highest chance.</li>
 * <li>Uses exponential scaling (2^(maxCount - count)) for smoother weight adjustment.</li>
 * </ul>
 * <p>
 * Supported types:
 * <ul>
 * <li>Integer (default range-based selection)</li>
 * <li>Boolean (weighted random true/false selection)</li>
 * <li>Float and Double (randomized within a range, with pity weighting applied)</li>
 * </ul>
 * <p>
 * Probability Breakdown:
 * <ul>
 * <li>If all values have been picked equally, they each have a 1/N chance.</li>
 * <li>If one value has been picked more times than another, its weight is halved per additional selection.</li>
 * <li>Example: If we have (1,2,3,4,5) and 3 has been picked twice while others once, then 3's weight is reduced by half.</li>
 * </ul>
 * <p>
 * Math formula for weighting:
 * <ul>
 * <li>For each value <code>x</code>, its weight <code>w(x)</code> is calculated as:</li>
 *
 * <pre>
 * <code>w(x) = 2^(maxCount - count(x))</code>
 * </pre>
 *
 * <li>Where <code>maxCount</code> is the highest count of selections made for any value, and <code>count(x)</code> is the number of times <code>x</code> has been selected.</li>
 * <li>The weight decreases exponentially for values with higher selection counts, and increases for values with fewer selections, encouraging the selection of less-picked values.</li>
 * </ul>
 * <p>
 * In other words:
 * <ul>
 * <li>If <code>x</code> has been picked once, its weight is <code>2^(maxCount - 1)</code>.</li>
 * <li>If <code>x</code> has been picked twice, its weight is <code>2^(maxCount - 2)</code>.</li>
 * <li>This exponential decay ensures that values picked more frequently are less likely to be selected again, while those picked less frequently are more likely to be selected.</li>
 * </ul>
 */
@SuppressWarnings("unused")
@ApiStatus.Experimental
public class PityRandom {

	@NotNull
	private final Integer minInteger;
	@NotNull
	private final Integer maxInteger;
	@NotNull
	private final Double minDouble;
	@NotNull
	private final Double maxDouble;
	@NotNull
	private final Map<Integer, Integer> selectionCountInteger;
	@NotNull
	private final Map<Double, Integer> selectionCountDouble;
	@NotNull
	private final Random random;

	public PityRandom(@NotNull Integer pMin, @NotNull Integer pMax) {
		this.minInteger = pMin;
		this.maxInteger = pMax;
		this.minDouble = pMin.doubleValue();
		this.maxDouble = pMax.doubleValue();
		this.selectionCountInteger = new HashMap<>();
		this.selectionCountDouble = new HashMap<>();
		this.random = new Random();

		for (Integer i = pMin; i <= pMax; i++) {
			selectionCountInteger.put(i, 0);
		}
	}

	@NotNull
	public Integer nextInteger() {
		Map<Integer, Double> weights = new HashMap<>();
		Integer maxCount = selectionCountInteger.values().stream().max(Integer::compareTo).orElse(1);

		for (Integer num = minInteger; num <= maxInteger; num++) {
			Integer count = selectionCountInteger.get(num);
			weights.put(num, Math.pow(2, maxCount - count));
		}

		Double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
		TreeMap<Double, Integer> probabilityMap = new TreeMap<>();
		double cumulative = 0.0;

		for (Map.Entry<Integer, Double> entry : weights.entrySet()) {
			cumulative += entry.getValue() / totalWeight;
			probabilityMap.put(cumulative, entry.getKey());
		}

		Double roll = random.nextDouble();
		Integer selected = probabilityMap.ceilingEntry(roll).getValue();
		selectionCountInteger.put(selected, selectionCountInteger.get(selected) + 1);
		return selected;
	}

	@NotNull
	public Boolean nextBoolean() {
		Integer trueCount = selectionCountInteger.getOrDefault(1, 0);
		Integer falseCount = selectionCountInteger.getOrDefault(0, 0);
		Integer maxCount = Math.max(trueCount, falseCount);

		Double trueWeight = Math.pow(2, maxCount - trueCount);
		Double falseWeight = Math.pow(2, maxCount - falseCount);
		Double totalWeight = trueWeight + falseWeight;

		boolean selected = (random.nextDouble() < (trueWeight / totalWeight));
		selectionCountInteger.put(selected ? 1 : 0, selectionCountInteger.getOrDefault(selected ? 1 : 0, 0) + 1);
		return selected;
	}

	@NotNull
	private Double getNextValue(@NotNull Double min, @NotNull Double max, @NotNull Map<Double, Integer> selectionCount) {
		Map<Double, Double> weights = new HashMap<>();
		double maxCount = selectionCount.values().stream().mapToInt(Integer::intValue).max().orElse(1);

		for (Double num = min; num <= max; num += 0.01) {
			Integer count = selectionCount.getOrDefault(num, 0);
			weights.put(num, Math.pow(2, maxCount - count));
		}

		Double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
		TreeMap<Double, Double> probabilityMap = new TreeMap<>();
		double cumulative = 0.0;

		for (Map.Entry<Double, Double> entry : weights.entrySet()) {
			cumulative += entry.getValue() / totalWeight;
			probabilityMap.put(cumulative, entry.getKey());
		}

		Double roll = random.nextDouble();
		Double selected = probabilityMap.ceilingEntry(roll).getValue();
		selectionCount.put(selected, selectionCount.getOrDefault(selected, 0) + 1);
		return selected;
	}

	@NotNull
	public Float nextFloat() {
		return getNextValue(minDouble, maxDouble, selectionCountDouble).floatValue();
	}

	@NotNull
	public Double nextDouble() {
		return getNextValue(minDouble, maxDouble, selectionCountDouble);
	}
}
