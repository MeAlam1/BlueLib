/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.AnimationProcessor;
import software.bluelib.loader.animation.math.Easing;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;

/**
 * A data container holding interpolation parameters for bone animation between two keyframes.
 *
 * <p>This record is a passive data carrier that stores the start/end values, timing,
 * and keyframe metadata needed to compute interpolated transform values.</p>
 *
 * @param keyframe         The source keyframe cache containing easing type. May be null during transitions.
 * @param currentTick      Current position within this interpolation segment.
 * @param transitionLength Total duration of this interpolation in ticks.
 * @param startValue       The transform value at the beginning of interpolation.
 * @param endValue         The transform value at the end of interpolation.
 * @see BoneAnimationFrame
 * @see AnimationProcessor#tickAnimation
 * @see Easing#lerpWithOverride
 */
public record InterpolationData(
		@Nullable KeyframeCache<?> keyframe,
		double currentTick,
		double transitionLength,
		double startValue,
		double endValue
) {

	/**
	 * Calculates the normalized progress (0.0 to 1.0) through this interpolation.
	 *
	 * @return Progress ratio, clamped between 0 and 1
	 */
	public double getProgress() {
		if (transitionLength <= 0) return 1.0;
		return Math.min(1.0, Math.max(0.0, currentTick / transitionLength));
	}

	@Override
	public @NotNull String toString() {
		return "InterpolationData{" +
				"tick=" + this.currentTick +
				", length=" + this.transitionLength +
				", start=" + this.startValue +
				", end=" + this.endValue +
				", progress=" + String.format("%.2f", getProgress()) +
				'}';
	}
}