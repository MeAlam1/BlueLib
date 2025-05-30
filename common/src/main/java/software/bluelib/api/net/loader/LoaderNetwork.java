package software.bluelib.api.net.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.util.GeckoLibUtil;
import software.bluelib.net.messages.client.loader.*;

import javax.annotation.Nullable;

public class LoaderNetwork extends NetworkRegistry {
	public static <D> void syncBlockEntityAnimData(BlockPos pPos, SerializableDataTicket<D> pDataTicket, D pData, ServerLevel pLevel) {
		sendToAllPlayersTrackingBlock(pLevel, pPos, new BlockEntityDataSyncPacket<>(pPos, pDataTicket, pData));
	}

	public static <D> void syncEntityAnimData(Entity entity, boolean pIsReplacedEntity, SerializableDataTicket<D> pDataTicket, D pData) {
		sendToAllPlayersTrackingEntity(entity, new EntityDataSyncPacket<>(entity.getId(), pIsReplacedEntity, pDataTicket, pData));
	}

	public static <D> void syncSingletonAnimData(GeoAnimatable pAnimatable, long pInstanceId, SerializableDataTicket<D> pDataTicket, D pData, Entity pEntityToTrack) {
		String syncedId = GeckoLibUtil.getSyncedSingletonAnimatableId(pAnimatable);
		sendToAllPlayersTrackingEntity(pEntityToTrack, new SingletonDataSyncPacket<>(syncedId, pInstanceId, pDataTicket, pData));
	}

	public static void triggerBlockEntityAnim(BlockPos pPos, @Nullable String pControllerName, String pAnimName, ServerLevel pLevel) {
		sendToAllPlayersTrackingBlock(pLevel, pPos, new BlockEntityAnimTriggerPacket(pPos, pControllerName == null ? "" : pControllerName, pAnimName));
	}

	public static void triggerEntityAnim(Entity pEntity, boolean pIsReplacedEntity, @Nullable String pControllerName, String pAnimName) {
		sendToAllPlayersTrackingEntity(pEntity, new EntityAnimTriggerPacket(pEntity.getId(), pIsReplacedEntity, pControllerName == null ? "" : pControllerName, pAnimName));
	}
	
	public static void triggerSingletonAnim(String pAnimatableClassName, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
		sendToAllPlayersTrackingEntity(pEntityToTrack, new SingletonAnimTriggerPacket(pAnimatableClassName, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName));
	}

	public static void triggerSingletonAnim(GeoAnimatable pAnimatable, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, String pAnimName) {
		String syncedId = GeckoLibUtil.getSyncedSingletonAnimatableId(pAnimatable);
		triggerSingletonAnim(syncedId, pEntityToTrack, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName);
	}

	public static void stopTriggeredBlockEntityAnim(BlockPos pPos, ServerLevel pLevel, @Nullable String pControllerName, @Nullable String pAnimName) {
		sendToAllPlayersTrackingBlock(pLevel, pPos, new StopTriggeredBlockEntityAnimPacket(pPos, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName));
	}

	public static void stopTriggeredEntityAnim(Entity pEntity, boolean pIsReplacedEntity, @Nullable String pControllerName, @Nullable String pAnimName) {
		sendToAllPlayersTrackingEntity(pEntity, new StopTriggeredEntityAnimPacket(pEntity.getId(), pIsReplacedEntity, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName));
	}

	public static void stopTriggeredSingletonAnim(GeoAnimatable pAnimatable, Entity pEntityToTrack, long pInstanceId, @Nullable String pControllerName, @Nullable String pAnimName) {
		String syncedId = GeckoLibUtil.getSyncedSingletonAnimatableId(pAnimatable);
		sendToAllPlayersTrackingEntity(pEntityToTrack, new StopTriggeredSingletonAnimPacket(syncedId, pInstanceId, pControllerName == null ? "" : pControllerName, pAnimName == null ? "" : pAnimName));
	}
}