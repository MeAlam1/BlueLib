/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animations.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.oldLoader.animation.EasingType;

public record KeyframeCache<T extends MathValue>(double length, @NotNull T startValue, @NotNull T endValue,
		@NotNull EasingType easingType, @NotNull List<T> easingArgs) {

	public KeyframeCache(double pLength, @NotNull T pStartValue, @NotNull T pEndValue) {
		this(pLength, pStartValue, pEndValue, EasingType.LINEAR);
	}

	public KeyframeCache(double pLength, @NotNull T pStartValue, @NotNull T pEndValue, @NotNull EasingType pEasingType) {
		this(pLength, pStartValue, pEndValue, pEasingType, new ObjectArrayList<>(0));
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.length, this.startValue, this.endValue, this.easingType, this.easingArgs);
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj)
			return true;

		if (pObj == null || getClass() != pObj.getClass())
			return false;

		return hashCode() == pObj.hashCode();
	}
}
