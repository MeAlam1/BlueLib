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

import java.util.ArrayList;
import java.util.List;

public record AnimationCache(
		@NotNull String name, // TODO: Since the Animations are stored in a Map by their names, This field is redundant and can be removed later.
		@Nullable Double length,
		@Nullable LoopTypeCache loopType,
		@NotNull List<BoneAnimationCache> boneAnimation,
		@Nullable List<SoundKeyframeCache> sounds,
		@Nullable List<ParticleKeyframeCache> particles,
		@Nullable List<CustomInstructionKeyframeCache> customInstructions) {

	@NotNull
	public static AnimationCache generateWaitAnimation(double pLength) {
		return new AnimationCache(Animation.Stage.WAIT, pLength, LoopTypeCache.PLAY_ONCE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

}
