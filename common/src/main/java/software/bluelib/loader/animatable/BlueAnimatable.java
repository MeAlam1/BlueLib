/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable;

import java.util.Map;
import java.util.WeakHashMap;
import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.InstancedAnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.SingletonAnimatableInstanceCache;
import software.bluelib.oldLoader.animation.AnimatableManager;

public interface BlueAnimatable {

	Map<BlueAnimatable, AnimatableInstanceCache> CACHE = new WeakHashMap<>();

	void registerControllers(AnimatableManager.ControllerRegistrar pControllers);

	default AnimatableInstanceCache getAnimatableInstanceCache() {
		AnimatableInstanceCache customCache = useCustomCache();
		if (customCache != null) {
			return customCache;
		}
		return CACHE.computeIfAbsent(this, k -> useSingletonCache()
				? new SingletonAnimatableInstanceCache(k)
				: new InstancedAnimatableInstanceCache(k));
	}

	default boolean useSingletonCache() {
		return false;
	}

	default double boneResetTime() {
		return 5;
	}

	default boolean playWhilePaused() {
		return false;
	}

	double getTick(Object pObject);

	default AnimatableInstanceCache useCustomCache() {
		return null;
	}
}
