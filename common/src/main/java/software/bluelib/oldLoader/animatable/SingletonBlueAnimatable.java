/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable;

import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.loader.animatable.AnimatableManager;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;
import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.SingletonAnimatableInstanceCache;

public interface SingletonBlueAnimatable extends BlueAnimatable {

	static void registerSyncedAnimatable(BlueAnimatable pAnimatable) {
		LoaderUtils.registerSyncedAnimatable(pAnimatable);
	}

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(long pInstanceId, SerializableDataTicket<D> pDataTicket) {
		return getAnimatableInstanceCache().getManagerForId(pInstanceId).getData(pDataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(Entity pRelatedEntity, long pInstanceId, SerializableDataTicket<D> pDataTicket, D pData) {
		if (pRelatedEntity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(pInstanceId).setData(pDataTicket, pData);
		} else {
			syncAnimData(pInstanceId, pDataTicket, pData, pRelatedEntity);
		}
	}

	@ApiStatus.NonExtendable
	default <D> void syncAnimData(long pInstanceId, SerializableDataTicket<D> pDataTicket, D pData, Entity pEntityToTrack) {
		LoaderNetwork.syncSingletonAnimData(this, pInstanceId, pDataTicket, pData, pEntityToTrack);
	}

	@ApiStatus.NonExtendable
	default <D> void triggerAnim(Entity pRelatedEntity, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
		if (pRelatedEntity.level().isClientSide()) {
			if (pControllerName != null) {
				getAnimatableInstanceCache().getManagerForId(pInstanceId).tryTriggerAnimation(pControllerName, pAnimName);
			} else {
				getAnimatableInstanceCache().getManagerForId(pInstanceId).tryTriggerAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.triggerSingletonAnim(this, pRelatedEntity, pInstanceId, pControllerName, pAnimName);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(Entity pRelatedEntity, long pInstanceId, @Nullable String pControllerName, @Nullable String pAnimName) {
		if (pRelatedEntity.level().isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(pInstanceId);

			if (animatableManager == null)
				return;

			if (pControllerName != null) {
				animatableManager.stopTriggeredAnimation(pControllerName, pAnimName);
			} else {
				animatableManager.stopTriggeredAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.stopTriggeredSingletonAnim(this, pRelatedEntity, pInstanceId, pControllerName, pAnimName);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerArmorAnim(Entity pRelatedEntity, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
		triggerAnim(pRelatedEntity, -pInstanceId, pControllerName, pAnimName);
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredArmorAnim(Entity pRelatedEntity, long pInstanceId, @Nullable String pControllerName, @Nullable String pAnimName) {
		stopTriggeredAnim(pRelatedEntity, -pInstanceId, pControllerName, pAnimName);
	}

	@Override
	default @NotNull AnimatableInstanceCache useCustomCache() {
		return new SingletonAnimatableInstanceCache(this);
	}

	default void createBlueRenderer(Consumer<BlueRenderProvider> pConsumer) {}

	default Object getRenderProvider() {
		return getAnimatableInstanceCache().getRenderProvider();
	}
}
