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
 * A generic randomizer with a "hard pity" system layered on top of the standard pity randomization.
 * <p>
 * This class extends {@link PityRandom}, retaining all standard pity random behavior:
 * <ul>
 * <li>Each call to {@link #nextValue()} uses the pity system to weight less-picked values higher.</li>
 * <li>The hard pity threshold guarantees that after a specified number of attempts, the least-picked value will be selected.</li>
 * <li>The pity system may select the least-picked value <b>before</b> the hard pity threshold is reached.</li>
 * <li>When the hard pity is triggered, or (optionally) when the least-picked value is selected by the pity system, all selection counts can be reset (configurable).</li>
 * </ul>
 * <p>
 * <b>Supported types:</b>
 * <ul>
 * <li>Any type (T) provided as a collection of values</li>
 * <li>Convenience factory methods are available for common types such as Integer, Boolean, Float, and Double</li>
 * </ul>
 * <p>
 * <b>How it works:</b>
 * <ul>
 * <li>Each call to {@link #nextValue()} increments an attempt counter.</li>
 * <li>If the number of attempts reaches the hard pity threshold, the least-picked value is forcibly selected, and all selection counts are reset.</li>
 * <li>If the least-picked value is selected by the pity system before the threshold, selection counts may also be reset (if configured).</li>
 * <li>After a hard pity trigger or reset, the attempt counter is reset.</li>
 * </ul>
 * <p>
 * <b>Use cases:</b>
 * <ul>
 * <li>Ensures fairness by guaranteeing a rare outcome after repeated failures, while still allowing for early success via the pity system.</li>
 * <li>Useful for systems like loot boxes or gacha, where both randomness and guaranteed outcomes are desired.</li>
 * </ul>
 * <p>
 * <b>Parameters:</b>
 * <ul>
 * <li><code>hardPity</code>: The number of attempts after which the hard pity is triggered.</li>
 * <li><code>resetOnAnyPity</code> (if implemented): Whether to reset selection counts when the least-picked value is selected by the pity system before the hard pity threshold.</li>
 * </ul>
 */
@SuppressWarnings("unused")
@ApiStatus.Experimental
public class HardPityRandom<T> extends PityRandom<T> {

	@NotNull
	private final Integer hardPity;
	@NotNull
	private Integer attempts = 0;

	public HardPityRandom(@NotNull Collection<T> pValues, @NotNull Integer pHardPity) {
		super(pValues);
		if (pHardPity < 0) {
			throw new IllegalArgumentException("Hard pity threshold must be non-negative");
		}
		this.hardPity = pHardPity;
	}

	@NotNull
	public static HardPityRandom<Double> ofRange(@NotNull Double pMin, @NotNull Double pMax, @NotNull Double pStep) {
		return ofRange(pMin, pMax, pStep, pMax.intValue());
	}

	@NotNull
	public static HardPityRandom<Double> ofRange(@NotNull Double pMax, @NotNull Double pStep, @NotNull Integer pHardPity) {
		return ofRange(0.0, pMax, pStep, pHardPity);
	}

	@NotNull
	public static HardPityRandom<Double> ofRange(@NotNull Double pMax, @NotNull Double pStep) {
		return ofRange(0.0, pMax, pStep, pMax.intValue());
	}

	@NotNull
	public static HardPityRandom<Double> ofRange(@NotNull Double pMin, @NotNull Double pMax, @NotNull Double pStep, @NotNull Integer pHardPity) {
		return new HardPityRandom<>(buildRange(pMin, pMax, pStep), pHardPity);
	}

	@NotNull
	private static List<Double> buildRange(@NotNull Double pStart, @NotNull Double pEnd, @NotNull Double pStep) {
		List<Double> range = new ArrayList<>();
		for (double d = pStart; d <= pEnd + 1e-9; d += pStep) {
			range.add(Math.round(d * 1_000_000.0) / 1_000_000.0);
		}
		return range;
	}

	@Override
	public @NotNull T nextValue() {
		attempts++;

		if (hardPity > 0 && attempts >= hardPity) {
			attempts = 0;

			T leastPicked = selectionCounts.entrySet().stream()
					.min(Comparator.comparingInt(Map.Entry::getValue))
					.map(Map.Entry::getKey)
					.orElse(values.getFirst());

			selectionCounts.put(leastPicked, selectionCounts.get(leastPicked) + 1);
			resetCounts();
			return leastPicked;
		}

		T value = super.nextValue();

		int minCount = selectionCounts.values().stream().min(Integer::compareTo).orElse(0);

		if (selectionCounts.get(value) == minCount + 1) {
			long stillMin = selectionCounts.values().stream().filter(c -> c == minCount).count();
			if (stillMin == 0) {
				resetCounts();
				attempts = 0;
			}
		}

		return value;
	}
}
