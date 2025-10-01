/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.cache.animation.keyframe.CustomInstructionKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.ParticleKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.SoundKeyframeCache;

import java.util.HashMap;

public record AnimationCache(
		@NotNull String name,
		@Nullable Double length,
		@Nullable LoopTypeCache loopType,
		@NotNull BoneAnimationsCache bones,
		@Nullable SoundKeyframeCache sounds,
		@Nullable ParticleKeyframeCache particles,
		@Nullable CustomInstructionKeyframeCache customInstructions) {

	@NotNull
	public static AnimationCache generateWaitAnimation(double pLength) {
		return new AnimationCache(Animation.Stage.WAIT, pLength, LoopTypeCache.PLAY_ONCE, new BoneAnimationsCache(new HashMap<>()), null, null, null);
	}
}
