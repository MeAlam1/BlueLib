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
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimatableManager;

public class SingletonAnimatableInstanceCache extends AnimatableInstanceCache {

	protected final Long2ObjectMap<AnimatableManager<?>> managers = new Long2ObjectOpenHashMap<>();

	public SingletonAnimatableInstanceCache(BlueAnimatable animatable) {
		super(animatable);
	}

	@Override
	public AnimatableManager<?> getManagerForId(long uniqueId) {
		if (!this.managers.containsKey(uniqueId))
			this.managers.put(uniqueId, new AnimatableManager<>(this.animatable));

		return this.managers.get(uniqueId);
	}
}
