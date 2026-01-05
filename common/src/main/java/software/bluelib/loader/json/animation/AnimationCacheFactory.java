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

				TransformKeyframeCache rotation = constructTransformKeyframe(deserializer.rotationArray(), deserializer.rotationObject());
				TransformKeyframeCache position = constructTransformKeyframe(deserializer.positionArray(), deserializer.positionObject());
				TransformKeyframeCache scale = constructTransformKeyframe(deserializer.scaleArray(), deserializer.scaleObject());

				boneAnimationMap.put(name, new BoneAnimationCache(name, rotation, position, scale));
			}

			return boneAnimationMap;
		}

		private @Nullable TransformKeyframeCache constructTransformKeyframe(
				@Nullable List<MoLangValue> pArrayData,
				@Nullable KeyframeDeserializer pObjectData) {
			if (pArrayData == null && pObjectData == null) {
				return null;
			}
			return new TransformKeyframeCache(pArrayData, constructKeyframeCache(pObjectData));
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

				if (deserializer.arrayData() != null) {
					keyframeDataMap.put(name, KeyframeCacheData.fromArray(deserializer.arrayData()));
				} else {
					keyframeDataMap.put(name, new KeyframeCacheData(
							deserializer.pre(),
							deserializer.post(),
							constructEasing(deserializer.easing()),
							deserializer.easingArgs()));
				}
			}
			return keyframeDataMap;
		}

		private @Nullable EasingCache constructEasing(@Nullable String pEasing) {
			if (pEasing == null) {
				return null;
			}
			return EasingCache.fromString(pEasing);
		}

		@Nullable
		private List<SoundKeyframeCache> constructSound(@Nullable List<SoundKeyframeDeserializer> pDeserializer) {
			if (pDeserializer == null) {
				return null;
			}
			List<SoundKeyframeCache> result = new java.util.ArrayList<>(pDeserializer.size());
			for (SoundKeyframeDeserializer d : pDeserializer) {
				result.add(new SoundKeyframeCache(d.startTick(), d.sound()));
			}
			return result;
		}

		@Nullable
		private List<ParticleKeyframeCache> constructParticle(@Nullable List<ParticleKeyframeDeserializer> pDeserializer) {
			if (pDeserializer == null) {
				return null;
			}
			List<ParticleKeyframeCache> result = new java.util.ArrayList<>(pDeserializer.size());
			for (ParticleKeyframeDeserializer d : pDeserializer) {
				result.add(new ParticleKeyframeCache(d.startTick(), d.effect(), d.locator(), d.script()));
			}
			return result;
		}

		@Nullable
		private List<CustomInstructionKeyframeCache> constructCustomInstruction(@Nullable List<CustomInstructionKeyframeDeserializer> pDeserializer) {
			if (pDeserializer == null) {
				return null;
			}
			List<CustomInstructionKeyframeCache> result = new java.util.ArrayList<>(pDeserializer.size());
			for (CustomInstructionKeyframeDeserializer d : pDeserializer) {
				result.add(new CustomInstructionKeyframeCache(d.startTick(), d.instructions()));
			}
			return result;
		}
	}
}
