/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import software.bluelib.loader.animation.EasingType;
import software.bluelib.loader.loading.math.MathValue;

import java.util.List;
import java.util.Objects;


public record Keyframe<T extends MathValue>(double length, T startValue, T endValue, EasingType easingType, List<T> easingArgs) {
	public Keyframe(double length, T startValue, T endValue) {
		this(length, startValue, endValue, EasingType.LINEAR);
	}

	public Keyframe(double length, T startValue, T endValue, EasingType easingType) {
		this(length, startValue, endValue, easingType, new ObjectArrayList<>(0));
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.length, this.startValue, this.endValue, this.easingType, this.easingArgs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		return hashCode() == obj.hashCode();
	}
}
