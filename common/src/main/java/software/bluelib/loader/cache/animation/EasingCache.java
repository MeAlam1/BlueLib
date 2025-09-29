/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.keyframe.AnimationPoint;

/**
 * TODO: Copy more Easing Types over from Easing file in {@link software.bluelib.loader.geckolib.animations.Easing}
 */
@SuppressWarnings("unused")
public record EasingCache(
		@NotNull String name,
		@NotNull Calculation calculation) {

	private static final @NotNull Map<String, EasingCache> ALL_EASINGS = new ConcurrentHashMap<>(64);

	@FunctionalInterface
	public interface Calculation {

		double apply(@NotNull AnimationPoint pPoint,
				@Nullable Double pEasingValue,
				double pLerpValue);
	}

	public EasingCache register() {
		ALL_EASINGS.put(name, this);
		return this;
	}

	public static @NotNull EasingCache fromString(@NotNull String pName) {
		return ALL_EASINGS.getOrDefault(pName.toLowerCase(Locale.ROOT), LINEAR);
	}

	public static final EasingCache LINEAR = new EasingCache("linear",
			(point, easingValue, lerp) -> Mth.lerp(lerp,
					point.animationStartValue(),
					point.animationEndValue())).register();

	public static final EasingCache EASE_IN_SINE = new EasingCache("easeinsine",
			(point, easingValue, lerp) -> Mth.lerp(sine(lerp),
					point.animationStartValue(),
					point.animationEndValue())).register();

	public static final EasingCache EASE_OUT_SINE = new EasingCache("easeoutsine",
			(point, easingValue, lerp) -> Mth.lerp(1 - sine(1 - lerp),
					point.animationStartValue(),
					point.animationEndValue())).register();

	static double linear(double pNumber) {
		return pNumber;
	}

	static double quadratic(double pNumber) {
		return pNumber * pNumber;
	}

	static double cubic(double pNumber) {
		return pNumber * pNumber * pNumber;
	}

	static double sine(double pNumber) {
		return 1 - Math.cos(pNumber * Math.PI / 2f);
	}

	static double circle(double pNumber) {
		return 1 - Math.sqrt(1 - pNumber * pNumber);
	}

	static double exp(double pNumber) {
		return Math.pow(2, 10 * (pNumber - 1));
	}
}
