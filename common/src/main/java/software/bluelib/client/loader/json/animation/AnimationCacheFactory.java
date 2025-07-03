/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bluelib.client.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.client.loader.json.CacheFactory;
import software.bluelib.client.loader.json.deserialize.animation.AnimationLibrary;

public interface AnimationCacheFactory extends CacheFactory<AnimationLibraryCache, AnimationLibrary> {

	Map<String, AnimationCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	AnimationCacheFactory DEFAULT_FACTORY = new Builtin();

	CacheFactory.Registry<AnimationLibraryCache, AnimationLibrary, AnimationCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

		@Override
		public Map<String, AnimationCacheFactory> factories() {
			return FACTORIES;
		}

		@Override
		public AnimationCacheFactory defaultFactory() {
			return DEFAULT_FACTORY;
		}
	};

	@Override
	default AnimationLibraryCache construct(AnimationLibrary pSource) {
		return constructBlueAnimator(pSource);
	}

	AnimationLibraryCache constructBlueAnimator(AnimationLibrary pAnimations);

	final class Builtin implements AnimationCacheFactory {

		@Override
		public AnimationLibraryCache constructBlueAnimator(AnimationLibrary pAnimations) {
			return pAnimations.animations();
		}
	}
}
