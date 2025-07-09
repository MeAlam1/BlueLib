/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.net.messages.client.loader.*;
import software.bluelib.oldLoader.constant.dataticket.SerializableDataTicket;

public class LoaderNetwork extends NetworkRegistry {

	public static <D> void syncBlockEntityAnimData(@NotNull BlockPos pPos, @NotNull SerializableDataTicket<D> pDataTicket, @NotNull D pData, @NotNull ServerLevel pLevel) {
		sendToAllPlayersTrackingBlock(pLevel, pPos, new BlockEntityDataSyncPacket<>(pPos, pDataTicket, pData));
	}

	public static <D> void syncEntityAnimData(@NotNull Entity entity, boolean pIsReplacedEntity, @NotNull SerializableDataTicket<D> pDataTicket, @NotNull D pData) {
		sendToAllPlayersTrackingEntity(entity, new EntityDataSyncPacket<>(entity.getId(), pIsReplacedEntity, pDataTicket, pData));
	}

	public static <D> void syncSingletonAnimData(@NotNull BlueAnimatable pAnimatable, @NotNull Long pInstanceId, @NotNull SerializableDataTicket<D> pDataTicket, @NotNull D pData, @NotNull Entity pEntityToTrack) {
		String syncedId = LoaderUtils.getSyncedSingletonAnimatableId(pAnimatable);
		sendToAllPlayersTrackingEntity(pEntityToTrack, new SingletonDataSyncPacket<>(syncedId, pInstanceId, pDataTicket, pData));
	}

	public static void triggerBlockEntityAnim(@NotNull BlockPos pPos, @Nullable String pControllerName, @NotNull String pAnimName, @NotNull ServerLevel pLevel) {
		sendToAllPlayersTrackingBlock(pLevel, pPos, new BlockEntityAnimTriggerPacket(pPos, pControllerName == null ? "" : pControllerName, pAnimName));
	}

	public static void triggerEntityAnim(@NotNull Entity pEntity, boolean pIsReplacedEntity, @Nullable String pControllerName, @NotNull String pAnimName) {
		sendToAllPlayersTrackingEntity(pEntity, new EntityAnimTriggerPacket(pEntity.getId(), pIsReplacedEntity, pControllerName == null ? "" : pControllerName, pAnimName));
	}

	public static void triggerSingletonAnim(@NotNull String pAnimatableClassName, @NotNull Entity pEntityToTrack, @NotNull Long pInstanceId, @Nullable String pControllerName, @NotNull String pAnimName) {
		sendToAllPlayersTrackingEntity(pEntityToTrack, new SingletonAnimTriggerPacket(pAnimatableClassName, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName));
	}

	public static void triggerSingletonAnim(@NotNull BlueAnimatable pAnimatable, @NotNull Entity pEntityToTrack, @NotNull Long pInstanceId, @Nullable String pControllerName, @NotNull String pAnimName) {
		String syncedId = LoaderUtils.getSyncedSingletonAnimatableId(pAnimatable);
		triggerSingletonAnim(syncedId, pEntityToTrack, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName);
	}

	public static void stopTriggeredBlockEntityAnim(@NotNull BlockPos pPos, @NotNull ServerLevel pLevel, @Nullable String pControllerName, @Nullable String pAnimName) {
		sendToAllPlayersTrackingBlock(pLevel, pPos, new StopTriggeredBlockEntityAnimPacket(pPos, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName));
	}

	public static void stopTriggeredEntityAnim(@NotNull Entity pEntity, boolean pIsReplacedEntity, @Nullable String pControllerName, @Nullable String pAnimName) {
		sendToAllPlayersTrackingEntity(pEntity, new StopTriggeredEntityAnimPacket(pEntity.getId(), pIsReplacedEntity, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName));
	}

	public static void stopTriggeredSingletonAnim(@NotNull BlueAnimatable pAnimatable, @NotNull Entity pEntityToTrack, @NotNull Long pInstanceId, @Nullable String pControllerName, @Nullable String pAnimName) {
		String syncedId = LoaderUtils.getSyncedSingletonAnimatableId(pAnimatable);
		sendToAllPlayersTrackingEntity(pEntityToTrack, new StopTriggeredSingletonAnimPacket(syncedId, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName));
	}
}
