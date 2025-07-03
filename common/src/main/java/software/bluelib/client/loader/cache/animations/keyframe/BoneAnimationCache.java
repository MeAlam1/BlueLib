/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.animations.keyframe;

import software.bluelib.oldLoader.loading.math.MathValue;

public record BoneAnimationCache(String boneName,
		KeyframeStackCache<KeyframeCache<MathValue>> rotationKeyFrames,
		KeyframeStackCache<KeyframeCache<MathValue>> positionKeyFrames,
		KeyframeStackCache<KeyframeCache<MathValue>> scaleKeyFrames) {}
