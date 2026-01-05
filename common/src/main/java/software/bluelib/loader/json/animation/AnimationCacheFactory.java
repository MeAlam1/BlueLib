/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.cache.animation.*;
import software.bluelib.loader.cache.animation.keyframe.*;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.animation.*;
import software.bluelib.loader.json.deserialize.animation.keyframe.*;

public interface AnimationCacheFactory extends CacheFactory<AnimationFileCache, AnimationFileDeserializer> {

	@NotNull
	Map<String, AnimationCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	@NotNull
	AnimationCacheFactory DEFAULT_FACTORY = new Builtin();

	@NotNull
	CacheFactory.Registry<AnimationFileCache, AnimationFileDeserializer, AnimationCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

		@Override
		public @NotNull Map<String, AnimationCacheFactory> factories() {
			return FACTORIES;
		}

		@Override
		public @NotNull AnimationCacheFactory defaultFactory() {
			return DEFAULT_FACTORY;
		}
	};

	@Override
	default @NotNull AnimationFileCache construct(@NotNull AnimationFileDeserializer pSource) {
		return constructAnimationFile(pSource);
	}

	@NotNull
	AnimationFileCache constructAnimationFile(@NotNull AnimationFileDeserializer pAnimationsDeserializer);

	final class Builtin implements AnimationCacheFactory {

		@Override
		public @NotNull AnimationFileCache constructAnimationFile(@NotNull AnimationFileDeserializer pAnimationsDeserializer) {
			return new AnimationFileCache(
					pAnimationsDeserializer.formatVersion(),
					constructAnimations(pAnimationsDeserializer.animations()));
		}

		@NotNull
		private AnimationsCache constructAnimations(@NotNull AnimationsDeserializer pAnimations) {
			return new AnimationsCache(
					constructAnimation(pAnimations.animations()));
		}

		@NotNull
		private Map<String, AnimationCache> constructAnimation(@NotNull Map<String, AnimationDeserializer> pAnimations) {
			Map<String, AnimationCache> animationMap = new Object2ObjectOpenHashMap<>(pAnimations.size());

			for (Map.Entry<String, AnimationDeserializer> entry : pAnimations.entrySet()) {
				String name = entry.getKey();
				AnimationDeserializer deserializer = entry.getValue();
				animationMap.put(name, new AnimationCache(
						name,
						deserializer.length(),
						constructLoopType(deserializer.loopType()),
						constructBones(deserializer.bones()),
						constructSound(deserializer.sounds()),
						constructParticle(deserializer.particles()),
						constructCustomInstruction(deserializer.customInstructions())));
			}
			return animationMap;
		}

		@NotNull
		private LoopTypeCache constructLoopType(@Nullable String pLoopType) {
			if (pLoopType == null) {
				return LoopTypeCache.PLAY_ONCE;
			}

			if ("true".equalsIgnoreCase(pLoopType)) {
				return LoopTypeCache.LOOP;
			} else if ("false".equalsIgnoreCase(pLoopType)) {
				return LoopTypeCache.PLAY_ONCE;
			} else {
				return LoopTypeCache.fromString(pLoopType);
			}
		}

		private @NotNull BoneAnimationsCache constructBones(@NotNull BoneAnimationsDeserializer pBones) {
			return new BoneAnimationsCache(
					constructBoneAnimations(pBones.boneAnimations()));
		}

		private @NotNull Map<String, BoneAnimationCache> constructBoneAnimations(
				@NotNull Map<String, BoneAnimationDeserializer> pBoneAnimationDeserializerMap) {
			Map<String, BoneAnimationCache> boneAnimationMap = new Object2ObjectOpenHashMap<>(pBoneAnimationDeserializerMap.size());

			for (Map.Entry<String, BoneAnimationDeserializer> entry : pBoneAnimationDeserializerMap.entrySet()) {
				String name = entry.getKey();
				BoneAnimationDeserializer deserializer = entry.getValue();

				TransformChannelCache rotation = constructTransformChannel(deserializer.rotationArray(), deserializer.rotationObject());
				TransformChannelCache position = constructTransformChannel(deserializer.positionArray(), deserializer.positionObject());
				TransformChannelCache scale = constructTransformChannel(deserializer.scaleArray(), deserializer.scaleObject());

				boneAnimationMap.put(name, new BoneAnimationCache(name, rotation, position, scale));
			}

			return boneAnimationMap;
		}

		private @Nullable TransformChannelCache constructTransformChannel(
				@Nullable List<MoLangValue> pArray,
				@Nullable KeyframeDeserializer pObject) {

			KeyframeCache keyframes = constructKeyframeCache(pObject);
			if (keyframes != null) {
				return new TransformChannelCache.Keyframes(keyframes);
			}

			if (pArray != null) {
				return new TransformChannelCache.Array(pArray);
			}

			return null;
		}

		private @Nullable KeyframeCache constructKeyframeCache(@Nullable KeyframeDeserializer pKeyframeDeserializer) {
			if (pKeyframeDeserializer == null) {
				return null;
			}
			return new KeyframeCache(
					constructKeyframeDataCache(pKeyframeDeserializer.keyframeData()));
		}

		private @NotNull Map<String, KeyframeCacheData> constructKeyframeDataCache(@NotNull Map<String, KeyframeDataDeserializer> pKeyframeDataDeserializerMap) {
			Map<String, KeyframeCacheData> keyframeDataMap = new Object2ObjectOpenHashMap<>(pKeyframeDataDeserializerMap.size());

			for (Map.Entry<String, KeyframeDataDeserializer> entry : pKeyframeDataDeserializerMap.entrySet()) {
				String name = entry.getKey();
				KeyframeDataDeserializer deserializer = entry.getValue();
				keyframeDataMap.put(name, new KeyframeCacheData(
						deserializer.arrayData(),
						deserializer.pre(),
						deserializer.post(),
						constructEasing(deserializer.easing()),
						deserializer.easingArgs()));
			}
			return keyframeDataMap;
		}

		private @Nullable EasingCache constructEasing(@Nullable String pEasing) {
			if (pEasing == null) {
				return null;
			}
			return EasingCache.fromString(pEasing);
		}

		private static <T, R> @Nullable R constructCustomDataCache(
				@Nullable T pDeserializer,
				Function<T, R> pMapper) {
			if (pDeserializer == null) {
				return null;
			}
			return pMapper.apply(pDeserializer);
		}

		@Nullable
		private SoundKeyframeCache constructSound(@Nullable SoundKeyframeDeserializer pDeserializer) {
			return constructCustomDataCache(
					pDeserializer,
					d -> new SoundKeyframeCache(d.startTick(), d.sound()));
		}

		@Nullable
		private ParticleKeyframeCache constructParticle(@Nullable ParticleKeyframeDeserializer pDeserializer) {
			return constructCustomDataCache(
					pDeserializer,
					d -> new ParticleKeyframeCache(d.startTick(), d.effect(), d.locator(), d.script()));
		}

		@Nullable
		private CustomInstructionKeyframeCache constructCustomInstruction(@Nullable CustomInstructionKeyframeDeserializer pDeserializer) {
			return constructCustomDataCache(
					pDeserializer,
					d -> new CustomInstructionKeyframeCache(d.startTick(), d.instructions()));
		}
	}
}
