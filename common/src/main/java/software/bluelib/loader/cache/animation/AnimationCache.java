/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.LoopTypeCache;
import software.bluelib.loader.cache.animation.keyframe.CustomInstructionKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.ParticleKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.SoundKeyframeCache;

public record AnimationCache(
		@NotNull String name,
		double length,
		@NotNull LoopTypeCache loopType,
		@NotNull BoneAnimationCache[] boneAnimationCaches,
		@NotNull SoundKeyframeCache[] sounds,
		@NotNull ParticleKeyframeCache[] particles,
		@NotNull CustomInstructionKeyframeCache[] customInstructions) {

	@NotNull
	public static AnimationCache generateWaitAnimation(double pLength) {
		return new AnimationCache(Animation.Stage.WAIT, pLength, LoopTypeCache.PLAY_ONCE, new BoneAnimationCache[0],
				new SoundKeyframeCache[0], new ParticleKeyframeCache[0], new CustomInstructionKeyframeCache[0]);
	}

}
