package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.NetworkRegistry;

public class FabricNetworkManager implements NetworkManager {

	public static void registerClientPackets() {
		// Client receives S2C packets
		NetworkRegistry.getS2CPayloads().forEach(FabricPacketInfo::registerS2CPayload);
		// Client sends C2S packets (register payload type only)
		NetworkRegistry.getC2SPayloads().forEach(FabricPacketInfo::registerC2SPayload);
	}

	public static void registerServerPackets() {
		// Server receives C2S packets
		NetworkRegistry.getC2SPayloads().forEach(FabricPacketInfo::registerC2SPayload);
		// Server sends S2C packets (register payload type only)
		NetworkRegistry.getS2CPayloads().forEach(FabricPacketInfo::registerS2CPayload);
	}

	public static void registerClientHandlers() {
		// Only register handlers for packets the client receives (S2C)
		NetworkRegistry.getS2CPayloads().stream()
				.map(FabricPacketInfo::new)
				.forEach(FabricPacketInfo::registerClientHandler);
	}

	public static void registerServerHandlers() {
		// Only register handlers for packets the server receives (C2S)
		NetworkRegistry.getC2SPayloads().stream()
				.map(FabricPacketInfo::new)
				.forEach(FabricPacketInfo::registerServerHandler);
	}

	@Override
	public void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
		ServerPlayNetworking.send(pPlayer, pPacket);
	}

	@Override
	public void sendToServer(@NotNull NetworkPacket<?> pPacket) {
		ClientPlayNetworking.send(pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingEntity(@NotNull Entity pTrackingEntity, @NotNull NetworkPacket<?> pPacket) {
		if (pTrackingEntity instanceof ServerPlayer pl)
			sendPacketToPlayer(pl, pPacket);

		for (ServerPlayer player : PlayerLookup.tracking(pTrackingEntity)) {
			sendPacketToPlayer(player, pPacket);
		}
	}

	@Override
	public void sendToAllPlayersTrackingBlock(@NotNull ServerLevel pLevel, @NotNull BlockPos pBlockPos, @NotNull NetworkPacket<?> pPacket) {
		for (ServerPlayer player : PlayerLookup.tracking(pLevel, pBlockPos)) {
			sendPacketToPlayer(player, pPacket);
		}
	}
}