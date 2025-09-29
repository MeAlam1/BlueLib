/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.animations;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.cache.animation.keyframe.KeyframeCache;

public record KeyframeStackCache<T extends KeyframeCache<?>>(
		@NotNull List<T> xKeyframes,
		@NotNull List<T> yKeyframes,
		@NotNull List<T> zKeyframes) {

	public KeyframeStackCache() {
		this(new ObjectArrayList<>(), new ObjectArrayList<>(), new ObjectArrayList<>());
	}

	@NotNull
	public static <F extends KeyframeCache<?>> KeyframeStackCache<F> from(@NotNull KeyframeStackCache<F> pOtherStack) {
		return new KeyframeStackCache<>(pOtherStack.xKeyframes, pOtherStack.yKeyframes, pOtherStack.zKeyframes);
	}

	public double getLastKeyframeTime() {
		double xTime = 0;
		double yTime = 0;
		double zTime = 0;

		for (T frame : xKeyframes()) {
			xTime += frame.length();
		}

		for (T frame : yKeyframes()) {
			yTime += frame.length();
		}

		for (T frame : zKeyframes()) {
			zTime += frame.length();
		}

		return Math.max(xTime, Math.max(yTime, zTime));
	}
}
