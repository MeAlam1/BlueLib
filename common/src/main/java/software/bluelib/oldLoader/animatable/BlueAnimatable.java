/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable;

import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animation.AnimatableManager;

public interface BlueAnimatable {

	void registerControllers(AnimatableManager.ControllerRegistrar controllers);

	AnimatableInstanceCache getAnimatableInstanceCache();

	default double getBoneResetTime() {
		return 5;
	}

	default boolean shouldPlayAnimsWhileGamePaused() {
		return false;
	}

	double getTick(Object object);

	default AnimatableInstanceCache animatableCacheOverride() {
		return null;
	}
}
