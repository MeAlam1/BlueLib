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
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.keyframe.AnimationPoint;
import software.bluelib.loader.geckolib.math.MathValue;

@SuppressWarnings("unused")
@FunctionalInterface
public interface Easing {

	final Map<String, Easing> EASING_TYPES = new ConcurrentHashMap<>(64);

	Easing LINEAR = register("linear", register("none", value -> easeIn(Easing::linear)));
	Easing STEP = register("step", value -> easeIn(step(value)));
	Easing EASE_IN_SINE = register("easeinsine", value -> easeIn(Easing::sine));
	Easing EASE_OUT_SINE = register("easeoutsine", value -> easeOut(Easing::sine));
	Easing EASE_IN_OUT_SINE = register("easeinoutsine", value -> easeInOut(Easing::sine));
	Easing EASE_IN_QUAD = register("easeinquad", value -> easeIn(Easing::quadratic));
	Easing EASE_OUT_QUAD = register("easeoutquad", value -> easeOut(Easing::quadratic));
	Easing EASE_IN_OUT_QUAD = register("easeinoutquad", value -> easeInOut(Easing::quadratic));
	Easing EASE_IN_CUBIC = register("easeincubic", value -> easeIn(Easing::cubic));
	Easing EASE_OUT_CUBIC = register("easeoutcubic", value -> easeOut(Easing::cubic));
	Easing EASE_IN_OUT_CUBIC = register("easeinoutcubic", value -> easeInOut(Easing::cubic));
	Easing EASE_IN_QUART = register("easeinquart", value -> easeIn(pow(4.0)));
	Easing EASE_OUT_QUART = register("easeoutquart", value -> easeOut(pow(4.0)));
	Easing EASE_IN_OUT_QUART = register("easeinoutquart", value -> easeInOut(pow(4.0)));
	Easing EASE_IN_QUINT = register("easeinquint", value -> easeIn(pow(4.0)));
	Easing EASE_OUT_QUINT = register("easeoutquint", value -> easeOut(pow(5.0)));
	Easing EASE_IN_OUT_QUINT = register("easeinoutquint", value -> easeInOut(pow(5.0)));
	Easing EASE_IN_EXPO = register("easeinexpo", value -> easeIn(Easing::exp));
	Easing EASE_OUT_EXPO = register("easeoutexpo", value -> easeOut(Easing::exp));
	Easing EASE_IN_OUT_EXPO = register("easeinoutexpo", value -> easeInOut(Easing::exp));
	Easing EASE_IN_CIRC = register("easeincirc", value -> easeIn(Easing::circle));
	Easing EASE_OUT_CIRC = register("easeoutcirc", value -> easeOut(Easing::circle));
	Easing EASE_IN_OUT_CIRC = register("easeinoutcirc", value -> easeInOut(Easing::circle));
	Easing EASE_IN_BACK = register("easeinback", value -> easeIn(back(value)));
	Easing EASE_OUT_BACK = register("easeoutback", value -> easeOut(back(value)));
	Easing EASE_IN_OUT_BACK = register("easeinoutback", value -> easeInOut(back(value)));
	Easing EASE_IN_ELASTIC = register("easeinelastic", value -> easeIn(elastic(value)));
	Easing EASE_OUT_ELASTIC = register("easeoutelastic", value -> easeOut(elastic(value)));
	Easing EASE_IN_OUT_ELASTIC = register("easeinoutelastic", value -> easeInOut(elastic(value)));
	Easing EASE_IN_BOUNCE = register("easeinbounce", value -> easeIn(bounce(value)));
	Easing EASE_OUT_BOUNCE = register("easeoutbounce", value -> easeOut(bounce(value)));
	Easing EASE_IN_OUT_BOUNCE = register("easeinoutbounce", value -> easeInOut(bounce(value)));
	Easing CATMULLROM = register("catmullrom", new CatmullRomEasing());

	Double2DoubleFunction buildTransformer(@Nullable Double pValue);

	static double lerpWithOverride(AnimationPoint pAnimationPoint, Easing pOverride) {
		Easing easing = pOverride;

		if (pOverride == null)
			easing = pAnimationPoint.keyFrame() == null ? LINEAR : pAnimationPoint.keyFrame().easing();

		return easing.apply(pAnimationPoint);
	}

	default double apply(AnimationPoint pAnimationPoint) {
		Double easingVariable = null;

		if (pAnimationPoint.keyFrame() != null && !pAnimationPoint.keyFrame().easingArgs().isEmpty())
			easingVariable = pAnimationPoint.keyFrame().easingArgs().getFirst().get();

		return apply(pAnimationPoint, easingVariable, pAnimationPoint.currentTick() / pAnimationPoint.transitionLength());
	}

	default double apply(AnimationPoint pAnimationPoint, @Nullable Double pEasingValue, Double pLerpValue) {
		if (pAnimationPoint.currentTick() >= pAnimationPoint.transitionLength())
			return (float) pAnimationPoint.animationEndValue();

		return Mth.lerp(buildTransformer(pEasingValue).apply(pLerpValue), pAnimationPoint.animationStartValue(), pAnimationPoint.animationEndValue());
	}

	static Easing register(String pName, Easing pEasing) {
		EASING_TYPES.putIfAbsent(pName, pEasing);

		return pEasing;
	}

	static Easing fromJson(JsonElement pJson) {
		if (!(pJson instanceof JsonPrimitive primitive) || !primitive.isString())
			return LINEAR;

		return fromString(primitive.getAsString().toLowerCase(Locale.ROOT));
	}

	static Easing fromString(String pName) {
		return EASING_TYPES.getOrDefault(pName, Easing.LINEAR);
	}

	// ---> Easing Transition Type Functions <--- //

	static Double2DoubleFunction linear(Double2DoubleFunction pFunction) {
		return pFunction;
	}

	static double catmullRom(double n) {
		return 0.5d * (2d * (n + 1d) + 2d
				+ (2d * n - 5d * (n + 1d) + 4d * (n + 2d) - (n + 3d))
				+ (3d * (n + 1d) - n - 3d * (n + 2d) + (n + 3d)));
	}

	static Double2DoubleFunction easeIn(Double2DoubleFunction pFunction) {
		return pFunction;
	}

	static Double2DoubleFunction easeOut(Double2DoubleFunction pFunction) {
		return time -> 1 - pFunction.apply(1 - time);
	}

	static Double2DoubleFunction easeInOut(Double2DoubleFunction pFunction) {
		return time -> {
			if (time < 0.5d)
				return pFunction.apply(time * 2d) / 2d;

			return 1 - pFunction.apply((1 - time) * 2d) / 2d;
		};
	}

	// ---> Stepping Functions <--- //

	static Double2DoubleFunction stepPositive(Double2DoubleFunction pFunction) {
		return n -> n > 0 ? 1 : 0;
	}

	static Double2DoubleFunction stepNonNegative(Double2DoubleFunction pFunction) {
		return n -> n >= 0 ? 1 : 0;
	}

	// ---> Mathematical Functions <--- //

	static double linear(double n) {
		return n;
	}

	static double quadratic(double n) {
		return n * n;
	}

	static double cubic(double n) {
		return n * n * n;
	}

	static double sine(double n) {
		return 1 - Math.cos(n * Math.PI / 2f);
	}

	static double circle(double n) {
		return 1 - Math.sqrt(1 - n * n);
	}

	static double exp(double n) {
		return Math.pow(2, 10 * (n - 1));
	}

	// ---> Easing Curve Functions <--- //

	static Double2DoubleFunction elastic(Double pNumber) {
		double n2 = pNumber == null ? 1 : pNumber;

		return t -> 1 - Math.pow(Math.cos(t * Math.PI / 2f), 3) * Math.cos(t * n2 * Math.PI);
	}

	static Double2DoubleFunction bounce(Double pNumber) {
		final double n2 = pNumber == null ? 0.5d : pNumber;

		Double2DoubleFunction one = x -> 121f / 16f * x * x;
		Double2DoubleFunction two = x -> 121f / 4f * n2 * Math.pow(x - 6f / 11f, 2) + 1 - n2;
		Double2DoubleFunction three = x -> 121 * n2 * n2 * Math.pow(x - 9f / 11f, 2) + 1 - n2 * n2;
		Double2DoubleFunction four = x -> 484 * n2 * n2 * n2 * Math.pow(x - 10.5f / 11f, 2) + 1 - n2 * n2 * n2;

		return t -> Math.min(Math.min(one.apply(t), two.apply(t)), Math.min(three.apply(t), four.apply(t)));
	}

	static Double2DoubleFunction back(Double pNumber) {
		final double n2 = pNumber == null ? 1.70158d : pNumber * 1.70158d;

		return t -> t * t * ((n2 + 1) * t - n2);
	}

	static Double2DoubleFunction pow(Double pNumber) {
		return t -> Math.pow(t, pNumber);
	}

	static Double2DoubleFunction step(Double pNumber) {
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
		public Double2DoubleFunction buildTransformer(Double pValue) {
			return easeInOut(Easing::catmullRom);
		}

		@Override
		public double apply(AnimationPoint pAnimationPoint, Double pEasingValue, Double pLerpValue) {
			if (pAnimationPoint.currentTick() >= pAnimationPoint.transitionLength())
				return pAnimationPoint.animationEndValue();

			List<? extends MathValue> easingArgs = pAnimationPoint.keyFrame().easingArgs();

			if (easingArgs.size() < 2)
				return Mth.lerp(buildTransformer(pEasingValue).apply(pLerpValue), pAnimationPoint.animationStartValue(), pAnimationPoint.animationEndValue());

			return getPointOnSpline(pLerpValue, easingArgs.get(0).get(), pAnimationPoint.animationStartValue(), pAnimationPoint.animationEndValue(), easingArgs.get(1).get());
		}
	}
}
