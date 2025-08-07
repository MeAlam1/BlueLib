/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.entity;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;

public interface BlueEntity extends BlueAnimatable {

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(@NotNull SerializableDataTicket<D> pDataTicket) {
		return getAnimatableInstanceCache().getManagerForId(((Entity) this).getId()).getData(pDataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(@NotNull SerializableDataTicket<D> pDataTicket, @NotNull D pData) {
		Entity entity = (Entity) this;

		if (entity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(entity.getId()).setData(pDataTicket, pData);
		} else {
			LoaderNetwork.syncEntityAnimData(entity, false, pDataTicket, pData);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerAnim(@Nullable String pControllerName, @NotNull String pAnimName) {
		Entity entity = (Entity) this;

		if (entity.level().isClientSide()) {
			if (pControllerName != null) {
				getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(pControllerName, pAnimName);
			} else {
				getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.triggerEntityAnim(entity, false, pControllerName, pAnimName);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(@Nullable String pControllerName, @Nullable String pAnimName) {
		Entity entity = (Entity) this;

		if (entity.level().isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(entity.getId());

			if (pControllerName != null) {
				animatableManager.stopTriggeredAnimation(pControllerName, pAnimName);
			} else {
				animatableManager.stopTriggeredAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.stopTriggeredEntityAnim(entity, false, pControllerName, pAnimName);
		}
	}

	@Override
	default @NotNull Double getTick(@NotNull Object pEntity) {
		return (double) ((Entity) pEntity).tickCount;
	}
}
