/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.loader.animatable.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;

public interface BlueEntity extends BlueAnimatable {

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
		return getAnimatableInstanceCache().getManagerForId(((Entity) this).getId()).getData(dataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
		Entity entity = (Entity) this;

		if (entity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(entity.getId()).setData(dataTicket, data);
		} else {
			LoaderNetwork.syncEntityAnimData(entity, false, dataTicket, data);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerAnim(@Nullable String controllerName, String animName) {
		Entity entity = (Entity) this;

		if (entity.level().isClientSide()) {
			if (controllerName != null) {
				getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(controllerName, animName);
			} else {
				getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(animName);
			}
		} else {
			LoaderNetwork.triggerEntityAnim(entity, false, controllerName, animName);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(@Nullable String controllerName, @Nullable String animName) {
		Entity entity = (Entity) this;

		if (entity.level().isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(entity.getId());

			if (animatableManager == null)
				return;

			if (controllerName != null) {
				animatableManager.stopTriggeredAnimation(controllerName, animName);
			} else {
				animatableManager.stopTriggeredAnimation(animName);
			}
		} else {
			LoaderNetwork.stopTriggeredEntityAnim(entity, false, controllerName, animName);
		}
	}

	@Override
	default @NotNull Double getTick(@NotNull Object pEntity) {
		return (double) ((Entity) pEntity).tickCount;
	}
}
