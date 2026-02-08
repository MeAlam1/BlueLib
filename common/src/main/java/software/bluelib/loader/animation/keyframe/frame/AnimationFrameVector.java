/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.frame;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.keyframe.InterpolationData;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;

public record AnimationFrameVector(@NotNull AnimationFrame x,
		@NotNull AnimationFrame y,
		@NotNull AnimationFrame z) {

	public AnimationFrameVector() {
		this(new AnimationFrame(), new AnimationFrame(), new AnimationFrame());
	}

	public void addPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
			double pStartX, double pEndX,
			double pStartY, double pEndY,
			double pStartZ, double pEndZ) {
		x.add(new InterpolationData(pKeyFrame, pLerpedTick, pTransitionLength, pStartX, pEndX));
		y.add(new InterpolationData(pKeyFrame, pLerpedTick, pTransitionLength, pStartY, pEndY));
		z.add(new InterpolationData(pKeyFrame, pLerpedTick, pTransitionLength, pStartZ, pEndZ));
	}

	public void add(@NotNull InterpolationData pXPoint, @NotNull InterpolationData pYPoint, @NotNull InterpolationData pZPoint) {
		x.add(pXPoint);
		y.add(pYPoint);
		z.add(pZPoint);
	}
}
