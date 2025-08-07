/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.base;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.animatable.cache.AnimatableInstanceCache;
import software.bluelib.loader.animatable.cache.InstancedAnimatableInstanceCache;
import software.bluelib.loader.animatable.cache.SingletonAnimatableInstanceCache;

public interface BlueAnimatable {

	@NotNull
	Map<BlueAnimatable, AnimatableInstanceCache<BlueAnimatable>> CACHE = new WeakHashMap<>();

	@NotNull
	ResourceLocation getControllerResource();

	@WillBeDeprecated(since = "2.3.1", reason = "This method will be revised due to the new Data Driven Controller System. Please migrate to the recommended alternatives.", alternatives = {
			"Use the new Data Driven Controller System.",
			"Refer to: data/MODID/controller/ENTITY.controller.json"
	})

	default <T extends BlueAnimatable> void registerControllers(@NotNull AnimatableManager.ControllerRegistrar<T> pRegistrar) {}

	@NotNull
	default AnimatableInstanceCache<? extends BlueAnimatable> getAnimatableInstanceCache() {
		AnimatableInstanceCache<? extends BlueAnimatable> customCache = useCustomCache();
		if (customCache != null) {
			return customCache;
		}
		return CACHE.computeIfAbsent(this, k -> useSingletonCache()
				? new SingletonAnimatableInstanceCache<>(k)
				: new InstancedAnimatableInstanceCache<>(k));
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
	default AnimatableInstanceCache<? extends BlueAnimatable> useCustomCache() {
		return null;
	}
}
