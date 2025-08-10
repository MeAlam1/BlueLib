/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.block;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.registry.LoaderNetwork;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;

public interface BlueBlockEntity extends BlueAnimatable {

	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(@NotNull SerializableDataTicket<D> pDataTicket) {
		return getAnimatableInstanceCache().getManagerForId(0).getData(pDataTicket);
	}

	@ApiStatus.NonExtendable
	default <D> void setAnimData(@NotNull SerializableDataTicket<D> pDataTicket, @NotNull D pData) {
		BlockEntity blockEntity = (BlockEntity) this;
		Level level = blockEntity.getLevel();

		if (level == null) {
			BaseLogger.log(BaseLogLevel.ERROR, "Attempting to set animation data for BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass() + ")");
			return;
		}

		if (level.isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(0).setData(pDataTicket, pData);
		} else {
			LoaderNetwork.syncBlockEntityAnimData(blockEntity.getBlockPos(), pDataTicket, pData, (ServerLevel) level);
		}
	}

	@ApiStatus.NonExtendable
	default void triggerAnim(@Nullable String pControllerName, @NotNull String pAnimName) {
		BlockEntity blockEntity = (BlockEntity) this;
		Level level = blockEntity.getLevel();

		if (level == null) {
			BaseLogger.log(BaseLogLevel.ERROR, "Attempting to trigger an animation for a BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass() + ")");
			return;
		}

		if (level.isClientSide()) {
			if (pControllerName != null) {
				getAnimatableInstanceCache().getManagerForId(0).tryTriggerAnimation(pControllerName, pAnimName);
			} else {
				getAnimatableInstanceCache().getManagerForId(0).tryTriggerAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.triggerBlockEntityAnim(blockEntity.getBlockPos(), pControllerName, pAnimName, (ServerLevel) level);
		}
	}

	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(@Nullable String pControllerName, @Nullable String pAnimName) {
		BlockEntity blockEntity = (BlockEntity) this;
		Level level = blockEntity.getLevel();

		if (level == null) {
			BaseLogger.log(BaseLogLevel.ERROR, "Attempting to stop a triggered animation for a BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass() + ")");
			return;
		}

		if (level.isClientSide()) {
			AnimatableManager<BlueAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(0);

			if (pControllerName != null) {
				animatableManager.stopTriggeredAnimation(pControllerName, pAnimName);
			} else {
				animatableManager.stopTriggeredAnimation(pAnimName);
			}
		} else {
			LoaderNetwork.stopTriggeredBlockEntityAnim(blockEntity.getBlockPos(), (ServerLevel) level, pControllerName, pAnimName);
		}
	}

	@Override
	default @NotNull Double getTick(@NotNull Object pBlockEntity) {
		return RenderUtils.getCurrentTick();
	}
}
