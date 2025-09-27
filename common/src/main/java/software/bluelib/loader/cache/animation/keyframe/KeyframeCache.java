/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.animation.math.Easing;
import software.bluelib.loader.geckolib.math.MathValue;

public record KeyframeCache<T extends MoLangValue>(
		double length, 
		@NotNull T startValue, 
		@NotNull T endValue,
		@NotNull Easing easing, 
		@NotNull List<T> easingArgs) {

	public KeyframeCache(double pLength, @NotNull T pStartValue, @NotNull T pEndValue) {
		this(pLength, pStartValue, pEndValue, Easing.LINEAR);
	}

	public KeyframeCache(double pLength, @NotNull T pStartValue, @NotNull T pEndValue, @NotNull Easing pEasing) {
		this(pLength, pStartValue, pEndValue, pEasing, new ObjectArrayList<>(0));
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.length, this.startValue, this.endValue, this.easing, this.easingArgs);
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
