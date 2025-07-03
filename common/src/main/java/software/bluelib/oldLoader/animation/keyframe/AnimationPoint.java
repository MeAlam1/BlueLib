/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import software.bluelib.client.loader.cache.animations.keyframe.KeyframeCache;

public record AnimationPoint(KeyframeCache<?> keyFrame, double currentTick, double transitionLength, double animationStartValue, double animationEndValue) {

	@Override
	public @NotNull String toString() {
		return "Tick: " + this.currentTick +
				" | Transition Length: " + this.transitionLength +
				" | Start Value: " + this.animationStartValue +
				" | End Value: " + this.animationEndValue;
	}
}
