/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable.instance;

import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimatableManager;

public class InstancedAnimatableInstanceCache extends AnimatableInstanceCache {

	protected AnimatableManager<?> manager;

	public InstancedAnimatableInstanceCache(BlueAnimatable pAnimatable) {
		super(pAnimatable);
	}

	@Override
	public AnimatableManager<?> getManagerForId(long pUniqueId) {
		if (this.manager == null)
			this.manager = new AnimatableManager<>(this.animatable);

		return this.manager;
	}
}
