/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable;

import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.InstancedAnimatableInstanceCache;
import software.bluelib.oldLoader.animation.AnimatableManager;

import java.util.Map;
import java.util.WeakHashMap;

public interface BlueAnimatable {

	Map<BlueAnimatable, AnimatableInstanceCache> _instanceCacheMap = new WeakHashMap<>();

	void registerControllers(AnimatableManager.ControllerRegistrar pControllers);

	default AnimatableInstanceCache getAnimatableInstanceCache() {
		return _instanceCacheMap.computeIfAbsent(this, InstancedAnimatableInstanceCache::new);
	}

	default double getBoneResetTime() {
		return 5;
	}

	default boolean shouldPlayAnimsWhileGamePaused() {
		return false;
	}

	double getTick(Object pObject);

	default AnimatableInstanceCache animatableCacheOverride() {
		return null;
	}
}
