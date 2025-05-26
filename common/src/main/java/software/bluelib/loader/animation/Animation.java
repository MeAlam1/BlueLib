/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.keyframe.BoneAnimation;
import software.bluelib.loader.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bluelib.loader.animation.keyframe.event.data.ParticleKeyframeData;
import software.bluelib.loader.animation.keyframe.event.data.SoundKeyframeData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public record Animation(String name, double length, LoopType loopType, BoneAnimation[] boneAnimations, Keyframes keyFrames) {
	public record Keyframes(SoundKeyframeData[] sounds, ParticleKeyframeData[] particles, CustomInstructionKeyframeData[] customInstructions) {}

	static Animation generateWaitAnimation(double length) {
		return new Animation(RawAnimation.Stage.WAIT, length, LoopType.PLAY_ONCE, new BoneAnimation[0],
				new Keyframes(new SoundKeyframeData[0], new ParticleKeyframeData[0], new CustomInstructionKeyframeData[0]));
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

		
		boolean shouldPlayAgain(GeoAnimatable animatable, AnimationController<? extends GeoAnimatable> controller, Animation currentAnimation);

		
		static LoopType fromJson(JsonElement json) {
			if (json == null || !json.isJsonPrimitive())
				return PLAY_ONCE;

			JsonPrimitive primitive = json.getAsJsonPrimitive();

			if (primitive.isBoolean())
				return primitive.getAsBoolean() ? LOOP : PLAY_ONCE;

			if (primitive.isString())
				return fromString(primitive.getAsString());

			return PLAY_ONCE;
		}

		static LoopType fromString(String name) {
			return LOOP_TYPES.getOrDefault(name, PLAY_ONCE);
		}

		
		static LoopType register(String name, LoopType loopType) {
			LOOP_TYPES.put(name, loopType);

			return loopType;
		}
	}
}
