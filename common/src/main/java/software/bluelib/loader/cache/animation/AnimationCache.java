/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.cache.animation.keyframe.CustomInstructionKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.ParticleKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.SoundKeyframeCache;

public record AnimationCache(
		@NotNull String name,
		@NotNull Double length,
		@Nullable LoopTypeCache loopType,
		@NotNull BoneAnimationsCache bones,
		@Nullable List<SoundKeyframeCache> sounds,
		@Nullable List<ParticleKeyframeCache> particles,
		@Nullable List<CustomInstructionKeyframeCache> customInstructions) {

	@NotNull
	public static AnimationCache generateWaitAnimation(double pLength) {
		return new AnimationCache(Animation.Stage.WAIT, pLength, LoopTypeCache.PLAY_ONCE, new BoneAnimationsCache(new HashMap<>()), null, null, null);
	}
}
