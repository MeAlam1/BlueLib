/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.math;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.keyframe.AnimationPoint;
import software.bluelib.loader.geckolib.math.MathValue;

@SuppressWarnings("unused")
@FunctionalInterface
public interface Easing {

	@NotNull
	Map<String, Easing> EASING_TYPES = new ConcurrentHashMap<>(64);

	@NotNull
	Easing LINEAR = register("linear", register("none", value -> easeIn(Easing::linear)));
	@NotNull
	Easing STEP = register("step", value -> easeIn(step(value)));
	@NotNull
	Easing EASE_IN_SINE = register("easeinsine", value -> easeIn(Easing::sine));
	@NotNull
	Easing EASE_OUT_SINE = register("easeoutsine", value -> easeOut(Easing::sine));
	@NotNull
	Easing EASE_IN_OUT_SINE = register("easeinoutsine", value -> easeInOut(Easing::sine));
	@NotNull
	Easing EASE_IN_QUAD = register("easeinquad", value -> easeIn(Easing::quadratic));
	@NotNull
	Easing EASE_OUT_QUAD = register("easeoutquad", value -> easeOut(Easing::quadratic));
	@NotNull
	Easing EASE_IN_OUT_QUAD = register("easeinoutquad", value -> easeInOut(Easing::quadratic));
	@NotNull
	Easing EASE_IN_CUBIC = register("easeincubic", value -> easeIn(Easing::cubic));
	@NotNull
	Easing EASE_OUT_CUBIC = register("easeoutcubic", value -> easeOut(Easing::cubic));
	@NotNull
	Easing EASE_IN_OUT_CUBIC = register("easeinoutcubic", value -> easeInOut(Easing::cubic));
	@NotNull
	Easing EASE_IN_QUART = register("easeinquart", value -> easeIn(pow(4.0)));
	@NotNull
	Easing EASE_OUT_QUART = register("easeoutquart", value -> easeOut(pow(4.0)));
	@NotNull
	Easing EASE_IN_OUT_QUART = register("easeinoutquart", value -> easeInOut(pow(4.0)));
	@NotNull
	Easing EASE_IN_QUINT = register("easeinquint", value -> easeIn(pow(4.0)));
	@NotNull
	Easing EASE_OUT_QUINT = register("easeoutquint", value -> easeOut(pow(5.0)));
	@NotNull
	Easing EASE_IN_OUT_QUINT = register("easeinoutquint", value -> easeInOut(pow(5.0)));
	@NotNull
	Easing EASE_IN_EXPO = register("easeinexpo", value -> easeIn(Easing::exp));
	@NotNull
	Easing EASE_OUT_EXPO = register("easeoutexpo", value -> easeOut(Easing::exp));
	@NotNull
	Easing EASE_IN_OUT_EXPO = register("easeinoutexpo", value -> easeInOut(Easing::exp));
	@NotNull
	Easing EASE_IN_CIRC = register("easeincirc", value -> easeIn(Easing::circle));
	@NotNull
	Easing EASE_OUT_CIRC = register("easeoutcirc", value -> easeOut(Easing::circle));
	@NotNull
	Easing EASE_IN_OUT_CIRC = register("easeinoutcirc", value -> easeInOut(Easing::circle));
	@NotNull
	Easing EASE_IN_BACK = register("easeinback", value -> easeIn(back(value)));
	@NotNull
	Easing EASE_OUT_BACK = register("easeoutback", value -> easeOut(back(value)));
	@NotNull
	Easing EASE_IN_OUT_BACK = register("easeinoutback", value -> easeInOut(back(value)));
	@NotNull
	Easing EASE_IN_ELASTIC = register("easeinelastic", value -> easeIn(elastic(value)));
	@NotNull
	Easing EASE_OUT_ELASTIC = register("easeoutelastic", value -> easeOut(elastic(value)));
	@NotNull
	Easing EASE_IN_OUT_ELASTIC = register("easeinoutelastic", value -> easeInOut(elastic(value)));
	@NotNull
	Easing EASE_IN_BOUNCE = register("easeinbounce", value -> easeIn(bounce(value)));
	@NotNull
	Easing EASE_OUT_BOUNCE = register("easeoutbounce", value -> easeOut(bounce(value)));
	@NotNull
	Easing EASE_IN_OUT_BOUNCE = register("easeinoutbounce", value -> easeInOut(bounce(value)));
	@NotNull
	Easing CATMULLROM = register("catmullrom", new CatmullRomEasing());

	@NotNull
	Double2DoubleFunction buildTransformer(@Nullable Double pValue);

	static double lerpWithOverride(@NotNull AnimationPoint pAnimationPoint, @Nullable Easing pOverride) {
		Easing easing = pOverride;

		if (pOverride == null)
			easing = pAnimationPoint.keyFrame() == null ? LINEAR : pAnimationPoint.keyFrame().easing();

		return easing.apply(pAnimationPoint);
	}

	default double apply(@NotNull AnimationPoint pAnimationPoint) {
		Double easingVariable = null;

		if (pAnimationPoint.keyFrame() != null && !pAnimationPoint.keyFrame().easingArgs().isEmpty())
			easingVariable = pAnimationPoint.keyFrame().easingArgs().getFirst().get();

		return apply(pAnimationPoint, easingVariable, pAnimationPoint.currentTick() / pAnimationPoint.transitionLength());
	}

	default double apply(@NotNull AnimationPoint pAnimationPoint, @Nullable Double pEasingValue, Double pLerpValue) {
		if (pAnimationPoint.currentTick() >= pAnimationPoint.transitionLength())
			return (float) pAnimationPoint.animationEndValue();

		return Mth.lerp(buildTransformer(pEasingValue).apply(pLerpValue), pAnimationPoint.animationStartValue(), pAnimationPoint.animationEndValue());
	}

	@NotNull
	static Easing register(@NotNull String pName, @NotNull Easing pEasing) {
		EASING_TYPES.putIfAbsent(pName, pEasing);

		return pEasing;
	}

	@NotNull
	static Easing fromJson(@NotNull JsonElement pJson) {
		if (!(pJson instanceof JsonPrimitive primitive) || !primitive.isString())
			return LINEAR;

		return fromString(primitive.getAsString().toLowerCase(Locale.ROOT));
	}

	@NotNull
	static Easing fromString(@NotNull String pName) {
		return EASING_TYPES.getOrDefault(pName, Easing.LINEAR);
	}

	// ---> Easing Transition Type Functions <--- //

	@NotNull
	static Double2DoubleFunction linear(@NotNull Double2DoubleFunction pFunction) {
		return pFunction;
	}

	static double catmullRom(double pNumber) {
		return 0.5d * (2d * (pNumber + 1d) + 2d
				+ (2d * pNumber - 5d * (pNumber + 1d) + 4d * (pNumber + 2d) - (pNumber + 3d))
				+ (3d * (pNumber + 1d) - pNumber - 3d * (pNumber + 2d) + (pNumber + 3d)));
	}

	@NotNull
	static Double2DoubleFunction easeIn(@NotNull Double2DoubleFunction pFunction) {
		return pFunction;
	}

	@NotNull
	static Double2DoubleFunction easeOut(@NotNull Double2DoubleFunction pFunction) {
		return time -> 1 - pFunction.apply(1 - time);
	}

	@NotNull
	static Double2DoubleFunction easeInOut(@NotNull Double2DoubleFunction pFunction) {
		return time -> {
			if (time < 0.5d)
				return pFunction.apply(time * 2d) / 2d;

			return 1 - pFunction.apply((1 - time) * 2d) / 2d;
		};
	}

	// ---> Stepping Functions <--- //

	@NotNull
	static Double2DoubleFunction stepPositive(@NotNull Double2DoubleFunction pFunction) {
		return n -> n > 0 ? 1 : 0;
	}

	@NotNull
	static Double2DoubleFunction stepNonNegative(@NotNull Double2DoubleFunction pFunction) {
		return n -> n >= 0 ? 1 : 0;
	}

	// ---> Mathematical Functions <--- //

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

	// ---> Easing Curve Functions <--- //

	@NotNull
	static Double2DoubleFunction elastic(@Nullable Double pNumber) {
		double n2 = pNumber == null ? 1 : pNumber;

		return t -> 1 - Math.pow(Math.cos(t * Math.PI / 2f), 3) * Math.cos(t * n2 * Math.PI);
	}

	@NotNull
	static Double2DoubleFunction bounce(@Nullable Double pNumber) {
		final double n2 = pNumber == null ? 0.5d : pNumber;

		Double2DoubleFunction one = x -> 121f / 16f * x * x;
		Double2DoubleFunction two = x -> 121f / 4f * n2 * Math.pow(x - 6f / 11f, 2) + 1 - n2;
		Double2DoubleFunction three = x -> 121 * n2 * n2 * Math.pow(x - 9f / 11f, 2) + 1 - n2 * n2;
		Double2DoubleFunction four = x -> 484 * n2 * n2 * n2 * Math.pow(x - 10.5f / 11f, 2) + 1 - n2 * n2 * n2;

		return t -> Math.min(Math.min(one.apply(t), two.apply(t)), Math.min(three.apply(t), four.apply(t)));
	}

	@NotNull
	static Double2DoubleFunction back(@Nullable Double pNumber) {
		final double n2 = pNumber == null ? 1.70158d : pNumber * 1.70158d;

		return t -> t * t * ((n2 + 1) * t - n2);
	}

	@NotNull
	static Double2DoubleFunction pow(@NotNull Double pNumber) {
		return t -> Math.pow(t, pNumber);
	}

	@NotNull
	static Double2DoubleFunction step(@Nullable Double pNumber) {
		double n2 = pNumber == null ? 2 : pNumber;

		if (n2 < 2)
			throw new IllegalArgumentException("Steps must be >= 2, got: " + n2);

		final int steps = (int) n2;

		return t -> {
			double result = 0;

			if (t < 0)
				return result;

			double stepLength = (1 / (double) steps);

			if (t > (result = (steps - 1) * stepLength))
				return result;

			int testIndex;
			int leftBorderIndex = 0;
			int rightBorderIndex = steps - 1;

			while (rightBorderIndex - leftBorderIndex != 1) {
				testIndex = leftBorderIndex + (rightBorderIndex - leftBorderIndex) / 2;

				if (t >= testIndex * stepLength) {
					leftBorderIndex = testIndex;
				} else {
					rightBorderIndex = testIndex;
				}
			}

			return leftBorderIndex * stepLength;
		};
	}

	class CatmullRomEasing implements Easing {

		// TODO: RENAME PARAMETERS
		public static double getPointOnSpline(double pDelta, double pP0, double pP1, double pP2, double pP3) {
			return 0.5d * (2d * pP1 + (pP2 - pP0) * pDelta +
					(2d * pP0 - 5d * pP1 + 4d * pP2 - pP3) * pDelta * pDelta +
					(3d * pP1 - pP0 - 3d * pP2 + pP3) * pDelta * pDelta * pDelta);
		}

		@Override
		public @NotNull Double2DoubleFunction buildTransformer(@Nullable Double pValue) {
			return easeInOut(Easing::catmullRom);
		}

		@Override
		public double apply(@NotNull AnimationPoint pAnimationPoint, @Nullable Double pEasingValue, @NotNull Double pLerpValue) {
			if (pAnimationPoint.currentTick() >= pAnimationPoint.transitionLength())
				return pAnimationPoint.animationEndValue();

			List<? extends MathValue> easingArgs = pAnimationPoint.keyFrame().easingArgs();

			if (easingArgs.size() < 2)
				return Mth.lerp(buildTransformer(pEasingValue).apply(pLerpValue), pAnimationPoint.animationStartValue(), pAnimationPoint.animationEndValue());

			return getPointOnSpline(pLerpValue, easingArgs.get(0).get(), pAnimationPoint.animationStartValue(), pAnimationPoint.animationEndValue(), easingArgs.get(1).get());
		}
	}
}
