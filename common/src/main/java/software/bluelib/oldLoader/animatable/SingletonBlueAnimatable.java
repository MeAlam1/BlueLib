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
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;
import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.SingletonAnimatableInstanceCache;
import software.bluelib.oldLoader.animation.AnimatableManager;
import software.bluelib.oldLoader.constant.dataticket.SerializableDataTicket;

public interface SingletonBlueAnimatable extends BlueAnimatable {

	static void registerSyncedAnimatable(BlueAnimatable animatable) {
		LoaderUtils.registerSyncedAnimatable(animatable);
	}

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(long instanceId, SerializableDataTicket<D> dataTicket) {
		return getAnimatableInstanceCache().getManagerForId(instanceId).getData(dataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(Entity relatedEntity, long instanceId, SerializableDataTicket<D> dataTicket, D data) {
		if (relatedEntity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(instanceId).setData(dataTicket, data);
		} else {
			syncAnimData(instanceId, dataTicket, data, relatedEntity);
		}
	}

	@ApiStatus.NonExtendable
	default <D> void syncAnimData(long instanceId, SerializableDataTicket<D> dataTicket, D data, Entity entityToTrack) {
		LoaderNetwork.syncSingletonAnimData(this, instanceId, dataTicket, data, entityToTrack);
	}

	@ApiStatus.NonExtendable
	default <D> void triggerAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
		if (relatedEntity.level().isClientSide()) {
			if (controllerName != null) {
				getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(controllerName, animName);
			} else {
				getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(animName);
			}
		} else {
			LoaderNetwork.triggerSingletonAnim(this, relatedEntity, instanceId, controllerName, animName);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, @Nullable String animName) {
		if (relatedEntity.level().isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(instanceId);

			if (animatableManager == null)
				return;

			if (controllerName != null) {
				animatableManager.stopTriggeredAnimation(controllerName, animName);
			} else {
				animatableManager.stopTriggeredAnimation(animName);
			}
		} else {
			LoaderNetwork.stopTriggeredSingletonAnim(this, relatedEntity, instanceId, controllerName, animName);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerArmorAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
		triggerAnim(relatedEntity, -instanceId, controllerName, animName);
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredArmorAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, @Nullable String animName) {
		stopTriggeredAnim(relatedEntity, -instanceId, controllerName, animName);
	}

	@Override
	default @Nullable AnimatableInstanceCache animatableCacheOverride() {
		return new SingletonAnimatableInstanceCache(this);
	}

	default void createBlueRenderer(Consumer<BlueRenderProvider> consumer) {}

	default Object getRenderProvider() {
		return getAnimatableInstanceCache().getRenderProvider();
	}
}
