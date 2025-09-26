/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.cache.animation.keyframe.KeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.KeyframeStackCache;
import software.bluelib.loader.geckolib.math.MathValue;

public record BoneAnimationCache(@NotNull String boneName,
		@NotNull KeyframeStackCache<KeyframeCache<MathValue>> rotationKeyFrames,
		@NotNull KeyframeStackCache<KeyframeCache<MathValue>> positionKeyFrames,
		@NotNull KeyframeStackCache<KeyframeCache<MathValue>> scaleKeyFrames) {}
