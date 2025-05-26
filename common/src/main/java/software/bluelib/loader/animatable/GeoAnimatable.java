/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable;

import software.bluelib.loader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.loader.animation.AnimatableManager;

public interface GeoAnimatable {

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
