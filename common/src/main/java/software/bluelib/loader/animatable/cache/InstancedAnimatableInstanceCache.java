/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;

public class InstancedAnimatableInstanceCache<T extends BlueAnimatable> extends AnimatableInstanceCache<T> {

	@Nullable
	protected AnimatableManager<T> manager;

	public InstancedAnimatableInstanceCache(@NotNull BlueAnimatable pAnimatable) {
		super(pAnimatable);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <M extends BlueAnimatable> @NotNull AnimatableManager<M> getManagerForId(long pUniqueId) {
		if (this.manager == null)
			this.manager = new AnimatableManager<>(this.animatable);

		return (AnimatableManager<M>) this.manager;
	}
}
