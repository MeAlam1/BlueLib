/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable.instance;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import software.bluelib.loader.animatable.AnimatableManager;
import software.bluelib.loader.animatable.BlueAnimatable;

public class SingletonAnimatableInstanceCache<T extends BlueAnimatable> extends AnimatableInstanceCache<T> {

	protected final Long2ObjectMap<AnimatableManager<? extends BlueAnimatable>> managers = new Long2ObjectOpenHashMap<>();

	public SingletonAnimatableInstanceCache(BlueAnimatable pAnimatable) {
		super(pAnimatable);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <M extends BlueAnimatable> AnimatableManager<M> getManagerForId(long pUniqueId) {
		if (!this.managers.containsKey(pUniqueId))
			this.managers.put(pUniqueId, new AnimatableManager<>(this.animatable));

		return (AnimatableManager<M>) this.managers.get(pUniqueId);
	}
}
