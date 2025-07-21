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
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;
import software.bluelib.oldLoader.animation.AnimatableManager;
import software.bluelib.oldLoader.constant.dataticket.SerializableDataTicket;

public interface BlueReplacedEntity extends SingletonBlueAnimatable {

	EntityType<?> getReplacingEntityType();

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(Entity entity, SerializableDataTicket<D> dataTicket) {
		return getAnimatableInstanceCache().getManagerForId(entity.getId()).getData(dataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(Entity relatedEntity, SerializableDataTicket<D> dataTicket, D data) {
		if (relatedEntity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(relatedEntity.getId()).setData(dataTicket, data);
		} else {
			LoaderNetwork.syncEntityAnimData(relatedEntity, true, dataTicket, data);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerAnim(Entity relatedEntity, @Nullable String controllerName, String animName) {
		if (relatedEntity.level().isClientSide()) {
			if (controllerName != null) {
				getAnimatableInstanceCache().getManagerForId(relatedEntity.getId()).tryTriggerAnimation(controllerName, animName);
			} else {
				getAnimatableInstanceCache().getManagerForId(relatedEntity.getId()).tryTriggerAnimation(animName);
			}
		} else {
			LoaderNetwork.triggerEntityAnim(relatedEntity, true, controllerName, animName);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(Entity relatedEntity, @Nullable String controllerName, @Nullable String animName) {
		if (relatedEntity.level().isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(relatedEntity.getId());

			if (animatableManager == null)
				return;

			if (controllerName != null) {
				animatableManager.stopTriggeredAnimation(controllerName, animName);
			} else {
				animatableManager.stopTriggeredAnimation(animName);
			}
		} else {
			LoaderNetwork.stopTriggeredEntityAnim(relatedEntity, true, controllerName, animName);
		}
	}

	@Override
	default double getTick(@NotNull Object pEntity) {
		return ((Entity) pEntity).tickCount;
	}

	// These methods aren't used for BlueReplacedEntity
	@ApiStatus.NonExtendable
	@Override
	default void createBlueRenderer(Consumer<BlueRenderProvider> pConsumer) {}

	// These methods aren't used for BlueReplacedEntity
	@ApiStatus.NonExtendable
	@Override
	default Object getRenderProvider() {
		return null;
	}
}
