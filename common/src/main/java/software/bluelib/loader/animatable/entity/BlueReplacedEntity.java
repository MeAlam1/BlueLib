/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.entity;

import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.base.SingletonBlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.renderer.client.BlueRenderProvider;

public interface BlueReplacedEntity extends SingletonBlueAnimatable {

	@NotNull
	EntityType<?> getReplacingEntityType();

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(@NotNull Entity pEntity, @NotNull SerializableDataTicket<D> pDataTicket) {
		return getAnimatableInstanceCache().getManagerForId(pEntity.getId()).getData(pDataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(@NotNull Entity pRelatedEntity, @NotNull SerializableDataTicket<D> pDataTicket, @NotNull D pData) {
		if (pRelatedEntity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(pRelatedEntity.getId()).setData(pDataTicket, pData);
		} else {
			LoaderNetwork.syncEntityAnimData(pRelatedEntity, true, pDataTicket, pData);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerAnim(@NotNull Entity pRelatedEntity, @Nullable String pControllerName, @NotNull String pAnimName) {
		if (pRelatedEntity.level().isClientSide()) {
			if (pControllerName != null) {
				getAnimatableInstanceCache().getManagerForId(pRelatedEntity.getId()).tryTriggerAnimation(pControllerName, pAnimName);
			} else {
				getAnimatableInstanceCache().getManagerForId(pRelatedEntity.getId()).tryTriggerAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.triggerEntityAnim(pRelatedEntity, true, pControllerName, pAnimName);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(@NotNull Entity pRelatedEntity, @Nullable String pControllerName, @Nullable String pAnimName) {
		if (pRelatedEntity.level().isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(pRelatedEntity.getId());

			if (pControllerName != null) {
				animatableManager.stopTriggeredAnimation(pControllerName, pAnimName);
			} else {
				animatableManager.stopTriggeredAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.stopTriggeredEntityAnim(pRelatedEntity, true, pControllerName, pAnimName);
		}
	}

	@Override
	default @NotNull Double getTick(@NotNull Object pEntity) {
		return (double) ((Entity) pEntity).tickCount;
	}

	/**
	 * This method is intentionally left empty because {@code ReplacedEntity} does not require a custom renderer.
	 * Prevents subclasses from overriding and bypassing the default implementation, which could result in the
	 * {@link BlueRenderProvider} not being created as expected.
	 *
	 * @param pConsumer a {@link Consumer} that would accept the {@link BlueRenderProvider}, but is unused here
	 */
	@ApiStatus.NonExtendable
	@Override
	default void createBlueRenderer(@NotNull Consumer<BlueRenderProvider> pConsumer) {}

	/**
	 * This method returns {@code null} because {@code ReplacedEntity} does not require a custom render provider.
	 * Prevents subclasses from overriding and bypassing the default implementation, which could interfere with
	 * the expected rendering behavior.
	 *
	 * @return {@code null} as no render provider is needed for this entity
	 */
	@ApiStatus.NonExtendable
	@Override
	default @Nullable Object getRenderProvider() {
		return null;
	}
}
