/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.instance;

import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animation.AnimatableManager;

public class InstancedAnimatableInstanceCache extends AnimatableInstanceCache {

    protected AnimatableManager<?> manager;

    public InstancedAnimatableInstanceCache(BlueAnimatable animatable) {
        super(animatable);
    }

    @Override
    public AnimatableManager<?> getManagerForId(long uniqueId) {
        if (this.manager == null)
            this.manager = new AnimatableManager<>(this.animatable);

        return this.manager;
    }
}
