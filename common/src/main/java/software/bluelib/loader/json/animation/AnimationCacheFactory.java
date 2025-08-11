/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.animation.AnimationLibrary;

public interface AnimationCacheFactory extends CacheFactory<AnimationLibraryCache, AnimationLibrary> {

	@NotNull
	Map<String, AnimationCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	@NotNull
	AnimationCacheFactory DEFAULT_FACTORY = new Builtin();

	@NotNull
	CacheFactory.Registry<AnimationLibraryCache, AnimationLibrary, AnimationCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

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
	default @Nullable AnimationLibraryCache construct(@NotNull AnimationLibrary pSource) {
		return constructBlueAnimator(pSource);
	}

	@Nullable
	AnimationLibraryCache constructBlueAnimator(@NotNull AnimationLibrary pAnimations);

	final class Builtin implements AnimationCacheFactory {

		@Override
		public @Nullable AnimationLibraryCache constructBlueAnimator(@NotNull AnimationLibrary pAnimations) {
			return pAnimations.animations();
		}
	}
}
