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
import software.bluelib.loader.cache.animation.*;
import software.bluelib.loader.cache.animation.keyframe.CustomInstructionKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.ParticleKeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.SoundKeyframeCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.animation.*;
import software.bluelib.loader.json.deserialize.animation.keyframe.CustomInstructionKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.ParticleKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.SoundKeyframeDeserializer;

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
					constructAnimations(pAnimationsDeserializer.animations())
			);
		}

		@NotNull
		private AnimationsCache constructAnimations(@NotNull AnimationsDeserializer pAnimations) {
			return new AnimationsCache(
					constructAnimation(pAnimations.animations())
			);
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
						constructSounds(deserializer.sounds()),
						constructParticles(deserializer.particles()),
						constructCustomInstructions(deserializer.customInstructions())
				));
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
					constructBoneAnimations(pBones.boneAnimations())
			);
		}

		private @NotNull Map<String, BoneAnimationCache> constructBoneAnimations(@NotNull Map<String, BoneAnimationDeserializer> pBoneAnimationDeserializerMap) {
			Map<String, BoneAnimationCache> boneAnimationMap = new Object2ObjectOpenHashMap<>(pBoneAnimationDeserializerMap.size());

			for (Map.Entry<String, BoneAnimationDeserializer> entry : pBoneAnimationDeserializerMap.entrySet()) {
				String name = entry.getKey();
				BoneAnimationDeserializer deserializer = entry.getValue();
				// TODO: Check which BoneType it is and construct accordingly.
			}
			return boneAnimationMap;
		}

		private static <T, R> @Nullable List<R> constructCustomDataCache(
				@Nullable List<T> pDeserializers,
				Function<T, R> pMapper
		) {
			if (pDeserializers == null || pDeserializers.isEmpty()) {
				return null;
			}
			return pDeserializers.stream().map(pMapper).toList();
		}

		@Nullable
		private List<SoundKeyframeCache> constructSounds(@Nullable List<SoundKeyframeDeserializer> pSoundKeyframeDeserializers) {
			return constructCustomDataCache(
					pSoundKeyframeDeserializers,
					pDeserializer -> new SoundKeyframeCache(pDeserializer.startTick(), pDeserializer.sound())
			);
		}

		@Nullable
		private List<ParticleKeyframeCache> constructParticles(@Nullable List<ParticleKeyframeDeserializer> pParticleKeyframeDeserializers) {
			return constructCustomDataCache(
					pParticleKeyframeDeserializers,
					pDeserializer -> new ParticleKeyframeCache(pDeserializer.startTick(), pDeserializer.effect(), pDeserializer.locator(), pDeserializer.script())
			);
		}

		@Nullable
		private List<CustomInstructionKeyframeCache> constructCustomInstructions(@Nullable List<CustomInstructionKeyframeDeserializer> pCustomInstructionKeyframeDeserializers) {
			return constructCustomDataCache(
					pCustomInstructionKeyframeDeserializers,
					pDeserializer -> new CustomInstructionKeyframeCache(pDeserializer.startTick(), pDeserializer.instructions())
			);
		}
	}
}
