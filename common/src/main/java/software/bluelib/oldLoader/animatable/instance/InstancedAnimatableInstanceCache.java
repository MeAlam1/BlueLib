/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable.instance;

import software.bluelib.loader.animatable.AnimatableManager;
import software.bluelib.loader.animatable.BlueAnimatable;

public class InstancedAnimatableInstanceCache<T extends BlueAnimatable> extends AnimatableInstanceCache<T> {

	protected AnimatableManager<T> manager;

	public InstancedAnimatableInstanceCache(BlueAnimatable pAnimatable) {
		super(pAnimatable);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <M extends BlueAnimatable> AnimatableManager<M> getManagerForId(long pUniqueId) {
		if (this.manager == null)
			this.manager = new AnimatableManager<>(this.animatable);

		return (AnimatableManager<M>) this.manager;
	}
}
