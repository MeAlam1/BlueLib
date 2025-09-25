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
import software.bluelib.loader.animation.LoopType;
import software.bluelib.loader.animation.keyframe.data.CustomInstructionKeyframeData;
import software.bluelib.loader.animation.keyframe.data.ParticleKeyframeData;
import software.bluelib.loader.animation.keyframe.data.SoundKeyframeData;
import software.bluelib.loader.cache.animation.keyframe.BoneAnimationCache;
import software.bluelib.loader.cache.animation.keyframe.KeyframeLibraryCache;

public record AnimationCache(
		@NotNull String name,
		double length,
		@NotNull LoopType loopType,
		@NotNull BoneAnimationCache[] boneAnimationCaches,
		@NotNull KeyframeLibraryCache keyFrames) {

	@NotNull
	public static AnimationCache generateWaitAnimation(double pLength) {
		return new AnimationCache(Animation.Stage.WAIT, pLength, LoopType.PLAY_ONCE, new BoneAnimationCache[0],
				new KeyframeLibraryCache(new SoundKeyframeData[0], new ParticleKeyframeData[0], new CustomInstructionKeyframeData[0]));
	}

}
