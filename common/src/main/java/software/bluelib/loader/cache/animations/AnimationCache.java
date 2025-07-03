/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animations;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import software.bluelib.loader.cache.animations.keyframe.BoneAnimationCache;
import software.bluelib.loader.cache.animations.keyframe.KeyframeLibraryCache;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimationController;
import software.bluelib.oldLoader.animation.RawAnimation;
import software.bluelib.oldLoader.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.ParticleKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.SoundKeyframeData;

public record AnimationCache(String name, double length, LoopType loopType, BoneAnimationCache[] boneAnimationCaches,
		KeyframeLibraryCache keyFrames) {

	public static AnimationCache generateWaitAnimation(double pLength) {
		return new AnimationCache(RawAnimation.Stage.WAIT, pLength, LoopType.PLAY_ONCE, new BoneAnimationCache[0],
				new KeyframeLibraryCache(new SoundKeyframeData[0], new ParticleKeyframeData[0], new CustomInstructionKeyframeData[0]));
	}

	@FunctionalInterface
	public interface LoopType {

		Map<String, LoopType> LOOP_TYPES = new ConcurrentHashMap<>(4);

		LoopType DEFAULT = (animatable, controller, currentAnimation) -> currentAnimation.loopType().shouldPlayAgain(animatable, controller, currentAnimation);
		LoopType PLAY_ONCE = register("play_once", register("false", (animatable, controller, currentAnimation) -> false));
		LoopType HOLD_ON_LAST_FRAME = register("hold_on_last_frame", (animatable, controller, currentAnimation) -> {
			controller.animationState = AnimationController.State.PAUSED;

			return true;
		});
		LoopType LOOP = register("loop", register("true", (animatable, controller, currentAnimation) -> true));

		boolean shouldPlayAgain(BlueAnimatable pAnimatable, AnimationController<? extends BlueAnimatable> pController, AnimationCache pCurrentAnimationCache);

		static LoopType fromJson(JsonElement pJson) {
			if (pJson == null || !pJson.isJsonPrimitive())
				return PLAY_ONCE;

			JsonPrimitive primitive = pJson.getAsJsonPrimitive();

			if (primitive.isBoolean())
				return primitive.getAsBoolean() ? LOOP : PLAY_ONCE;

			if (primitive.isString())
				return fromString(primitive.getAsString());

			return PLAY_ONCE;
		}

		static LoopType fromString(String pName) {
			return LOOP_TYPES.getOrDefault(pName, PLAY_ONCE);
		}

		static LoopType register(String pName, LoopType pLoopType) {
			LOOP_TYPES.put(pName, pLoopType);

			return pLoopType;
		}
	}
}
