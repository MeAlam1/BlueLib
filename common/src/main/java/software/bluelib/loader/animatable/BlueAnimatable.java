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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.InstancedAnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.SingletonAnimatableInstanceCache;

public interface BlueAnimatable {

	@NotNull
	Map<BlueAnimatable, AnimatableInstanceCache> CACHE = new WeakHashMap<>();

	@NotNull
	ResourceLocation getControllerResource();

	@WillBeDeprecated(since = "2.5.0", reason = "Due to the new Data Driven Controller System, ControllerRegistrar will be completely revised in the future.")
	default void registerControllers(@NotNull AnimatableManager.ControllerRegistrar pRegistrar) {}

	@NotNull
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

	@NotNull
	default Double boneResetTime() {
		return 5D;
	}

	default boolean playWhilePaused() {
		return false;
	}

	@NotNull
	Double getTick(@NotNull Object pObject);

	@Nullable
	default AnimatableInstanceCache useCustomCache() {
		return null;
	}
}
