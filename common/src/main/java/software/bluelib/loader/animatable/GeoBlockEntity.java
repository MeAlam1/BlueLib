/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.loader.LoaderNetwork;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;

public interface GeoBlockEntity extends GeoAnimatable {

    @ApiStatus.NonExtendable
    @Nullable
    default <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
        return getAnimatableInstanceCache().getManagerForId(0).getData(dataTicket);
    }

    @ApiStatus.NonExtendable
    default <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
        BlockEntity blockEntity = (BlockEntity) this;
        Level level = blockEntity.getLevel();

        if (level == null) {
            //GeckoLibConstants.LOGGER.error("Attempting to set animation data for BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass().toString() + ")");

            return;
        }

        if (level.isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(0).setData(dataTicket, data);
        } else {
            LoaderNetwork.syncBlockEntityAnimData(blockEntity.getBlockPos(), dataTicket, data, (ServerLevel) level);
        }
    }

    @ApiStatus.NonExtendable
    default void triggerAnim(@Nullable String controllerName, String animName) {
        BlockEntity blockEntity = (BlockEntity) this;
        Level level = blockEntity.getLevel();

        if (level == null) {
            //GeckoLibConstants.LOGGER.error("Attempting to trigger an animation for a BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass().toString() + ")");

            return;
        }

        if (level.isClientSide()) {
            if (controllerName != null) {
                getAnimatableInstanceCache().getManagerForId(0).tryTriggerAnimation(controllerName, animName);
            } else {
                getAnimatableInstanceCache().getManagerForId(0).tryTriggerAnimation(animName);
            }
        } else {
            LoaderNetwork.triggerBlockEntityAnim(blockEntity.getBlockPos(), controllerName, animName, (ServerLevel) level);
        }
    }

    @ApiStatus.NonExtendable
    default void stopTriggeredAnim(@Nullable String controllerName, @Nullable String animName) {
        BlockEntity blockEntity = (BlockEntity) this;
        Level level = blockEntity.getLevel();

        if (level == null) {
            //GeckoLibConstants.LOGGER.error("Attempting to stop a triggered animation for a BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass().toString() + ")");

            return;
        }

        if (level.isClientSide()) {
            AnimatableManager<GeoAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(0);

            if (controllerName != null) {
                animatableManager.stopTriggeredAnimation(controllerName, animName);
            } else {
                animatableManager.stopTriggeredAnimation(animName);
            }
        } else {
            LoaderNetwork.stopTriggeredBlockEntityAnim(blockEntity.getBlockPos(), (ServerLevel) level, controllerName, animName);
        }
    }

    @Override
    default double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();
    }
}
