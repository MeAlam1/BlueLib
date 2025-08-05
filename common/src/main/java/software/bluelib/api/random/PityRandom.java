/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.random;

import java.util.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <b>WARNING:</b> <i>Still a massive Work in Progress.</i> <br>
 * A generic randomizer with a pity system to balance probability.
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
 * <li>Any type (T) provided as a collection of values</li>
 * <li>Convenience factory methods can be used for common types such as Integer, Boolean, Float, and Double</li>
 * </ul>
 * <p>
 * Probability Breakdown:
 * <ul>
 * <li>If all values have been picked equally, they each have a 1/N chance.</li>
 * <li>If one value has been picked more times than another, its weight is halved per additional selection.</li>
 * <li>Example: If we have (A, B, C, D, E) and C has been picked twice while others once, then C's weight is reduced by half.</li>
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
public class PityRandom<T> extends Random {

	@NotNull
	protected final Map<T, Integer> selectionCounts = new LinkedHashMap<>();
	@NotNull
	protected final List<T> values;
	@NotNull
	protected Integer totalSelections = 0;

	public PityRandom(@NotNull Collection<T> pValues) {
		this.values = new ArrayList<>(pValues);
		for (T value : pValues) {
			selectionCounts.put(value, 0);
		}
	}

	@NotNull
	public static PityRandom<Double> ofRange(@NotNull Double pMin, @NotNull Double pMax, @NotNull Double pStep) {
		List<Double> range = new ArrayList<>();
		for (double d = pMin; d <= pMax + 1e-9; d += pStep) {
			range.add(Math.round(d * 1_000_000.0) / 1_000_000.0);
		}
		return new PityRandom<>(range);
	}

	@NotNull
	public T nextValue() {
		int maxCount = selectionCounts.values().stream().max(Integer::compareTo).orElse(1);
		Map<T, Double> weights = new LinkedHashMap<>();
		double totalWeight = 0.0;

		for (T value : values) {
			int count = selectionCounts.get(value);
			double weight = getWeight(value, count, maxCount);
			weights.put(value, weight);
			totalWeight += weight;
		}

		double roll = nextDouble() * totalWeight;
		double cumulative = 0.0;

		for (Map.Entry<T, Double> entry : weights.entrySet()) {
			cumulative += entry.getValue();
			if (roll <= cumulative) {
				T selected = entry.getKey();
				selectionCounts.put(selected, selectionCounts.get(selected) + 1);
				totalSelections++;
				return selected;
			}
		}

		return values.getFirst();
	}

	@NotNull
	protected Double getWeight(@NotNull T pValue, @NotNull Integer pCount, @NotNull Integer pMaxCount) {
		return Math.pow(2, pMaxCount - pCount);
	}

	public void resetCounts() {
		selectionCounts.replaceAll((k, v) -> 0);
		totalSelections = 0;
	}

	@NotNull
	public Integer getTotalSelections() {
		return totalSelections;
	}

	@NotNull
	public Map<T, Integer> getSelectionCounts() {
		return Collections.unmodifiableMap(selectionCounts);
	}
}
