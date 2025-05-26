/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.service;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.packet.*;
import software.bluelib.loader.util.GeckoLibUtil;

public interface GeckoLibNetworking {

    static void init() {
        registerPacket(BlockEntityAnimTriggerPacket.TYPE, BlockEntityAnimTriggerPacket.CODEC, true);
        registerPacket(BlockEntityDataSyncPacket.TYPE, BlockEntityDataSyncPacket.CODEC, true);
        registerPacket(EntityAnimTriggerPacket.TYPE, EntityAnimTriggerPacket.CODEC, true);
        registerPacket(EntityDataSyncPacket.TYPE, EntityDataSyncPacket.CODEC, true);
        registerPacket(SingletonAnimTriggerPacket.TYPE, SingletonAnimTriggerPacket.CODEC, true);
        registerPacket(SingletonDataSyncPacket.TYPE, SingletonDataSyncPacket.CODEC, true);
        registerPacket(StopTriggeredEntityAnimPacket.TYPE, StopTriggeredEntityAnimPacket.CODEC, true);
        registerPacket(StopTriggeredBlockEntityAnimPacket.TYPE, StopTriggeredBlockEntityAnimPacket.CODEC, true);
        registerPacket(StopTriggeredSingletonAnimPacket.TYPE, StopTriggeredSingletonAnimPacket.CODEC, true);
    }

    @ApiStatus.Internal
    private static <B extends FriendlyByteBuf, P extends MultiloaderPacket> void registerPacket(CustomPacketPayload.Type<P> pPayloadType, StreamCodec<B, P> pCodec, boolean pIsClientBound) {
        GeckoLibServices.NETWORK.registerPacketInternal(pPayloadType, pCodec, pIsClientBound);
    }

    @ApiStatus.Internal
    <B extends FriendlyByteBuf, P extends MultiloaderPacket> void registerPacketInternal(CustomPacketPayload.Type<P> pPayloadType, StreamCodec<B, P> pCodec, boolean pIsClientBound);

    void sendToAllPlayersTrackingEntity(MultiloaderPacket pPacket, Entity pTrackingEntity);

    void sendToAllPlayersTrackingBlock(MultiloaderPacket pPacket, ServerLevel pLevel, BlockPos pPos);

    void sendToPlayer(MultiloaderPacket pPacket, ServerPlayer player);

    default <D> void syncBlockEntityAnimData(BlockPos pPos, SerializableDataTicket<D> pDataTicket, D pData, ServerLevel pLevel) {
        sendToAllPlayersTrackingBlock(new BlockEntityDataSyncPacket<>(pPos, pDataTicket, pData), pLevel, pPos);
    }

    default <D> void syncEntityAnimData(Entity entity, boolean pIsReplacedEntity, SerializableDataTicket<D> pDataTicket, D pData) {
        sendToAllPlayersTrackingEntity(new EntityDataSyncPacket<>(entity.getId(), pIsReplacedEntity, pDataTicket, pData), entity);
    }

    @Deprecated(forRemoval = true)
    default <D> void syncSingletonAnimData(long pInstanceId, SerializableDataTicket<D> pDataTicket, D pData, Entity pEntityToTrack) {
        //sendToAllPlayersTrackingEntity(new SingletonDataSyncPacket<>(getClass().getName(), instanceId, dataTicket, data), entityToTrack);
    }

    @Deprecated(forRemoval = true)
    default <D> void syncSingletonAnimData(Class<?> pAnimatableClass, long pInstanceId, SerializableDataTicket<D> pDataTicket, D pData, Entity pEntityToTrack) {
        //sendToAllPlayersTrackingEntity(new SingletonDataSyncPacket<>(animatableClass.getName(), instanceId, dataTicket, data), entityToTrack);
    }

    default <D> void syncSingletonAnimData(GeoAnimatable pAnimatable, long pInstanceId, SerializableDataTicket<D> pDataTicket, D pData, Entity pEntityToTrack) {
        sendToAllPlayersTrackingEntity(new SingletonDataSyncPacket<>(GeckoLibUtil.getSyncedSingletonAnimatableId(pAnimatable), pInstanceId, pDataTicket, pData), pEntityToTrack);
    }

    default void triggerBlockEntityAnim(BlockPos pPos, @Nullable String pControllerName, String pAnimName, ServerLevel pLevel) {
        sendToAllPlayersTrackingBlock(new BlockEntityAnimTriggerPacket(pPos, pControllerName == null ? "" : pControllerName, pAnimName), pLevel, pPos);
    }

    default void triggerEntityAnim(Entity pEntity, boolean pIsReplacedEntity, @Nullable String pControllerName, String pAnimName) {
        sendToAllPlayersTrackingEntity(new EntityAnimTriggerPacket(pEntity.getId(), pIsReplacedEntity, pControllerName == null ? "" : pControllerName, pAnimName), pEntity);
    }

    @Deprecated(forRemoval = true)
    default void triggerSingletonAnim(String pAnimatableClassName, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
        sendToAllPlayersTrackingEntity(new SingletonAnimTriggerPacket(pAnimatableClassName, pInstanceId, pControllerName, pAnimName), pEntityToTrack);
    }

    @Deprecated(forRemoval = true)
    default void triggerSingletonAnim(Class<?> pAnimatableClass, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
        //triggerSingletonAnim(animatableClass.getName(), entityToTrack, instanceId, controllerName == null ? "" : controllerName, animName);
    }

    default void triggerSingletonAnim(GeoAnimatable pAnimatable, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
        triggerSingletonAnim(GeckoLibUtil.getSyncedSingletonAnimatableId(pAnimatable), pEntityToTrack, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName);
    }

    default void stopTriggeredBlockEntityAnim(BlockPos pPos, ServerLevel pLevel, @Nullable String pControllerName, @Nullable String pAnimName) {
        sendToAllPlayersTrackingBlock(new StopTriggeredBlockEntityAnimPacket(pPos, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName), pLevel, pPos);
    }

    default void stopTriggeredEntityAnim(Entity pEntity, boolean pIsReplacedEntity, @Nullable String pControllerName, @Nullable String pAnimName) {
        sendToAllPlayersTrackingEntity(new StopTriggeredEntityAnimPacket(pEntity.getId(), pIsReplacedEntity, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName), pEntity);
    }

    default void stopTriggeredSingletonAnim(GeoAnimatable pAnimatable, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, @Nullable String pAnimName) {
        sendToAllPlayersTrackingEntity(new StopTriggeredSingletonAnimPacket(GeckoLibUtil.getSyncedSingletonAnimatableId(pAnimatable), pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName), pEntityToTrack);
    }
}
