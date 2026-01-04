// file: `fabric/src/main/java/software/bluelib/net/FabricNetworkManager.java`
/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
		NetworkRegistry.getClientProvider().forEach(p -> {
			FabricPacketInfo.registerC2SPayload(p);
			FabricPacketInfo.registerS2CPayload(p);
		});

		FabricPacketInfo.registerClientHandlers(NetworkRegistry.getClientProvider());
	}

	public static void registerServerPackets() {
		NetworkRegistry.getServerProvider().forEach(p -> {
			FabricPacketInfo.registerC2SPayload(p);
			FabricPacketInfo.registerS2CPayload(p);
		});

		FabricPacketInfo.registerServerHandlers(NetworkRegistry.getServerProvider());
	}

	@Override
	public void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull software.bluelib.api.net.NetworkPacket<?> pPacket) {
		ServerPlayNetworking.send(pPlayer, pPacket);
	}

	@Override
	public void sendToServer(@NotNull NetworkPacket<?> pPacket) {
		ClientPlayNetworking.send(pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingEntity(@NotNull Entity pTrackingEntity, @NotNull NetworkPacket<?> pPacket) {
		if (pTrackingEntity instanceof ServerPlayer pl) sendPacketToPlayer(pl, pPacket);
		for (ServerPlayer player : PlayerLookup.tracking(pTrackingEntity)) sendPacketToPlayer(player, pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingBlock(@NotNull ServerLevel pLevel, @NotNull BlockPos pBlockPos, @NotNull NetworkPacket<?> pPacket) {
		for (ServerPlayer player : PlayerLookup.tracking(pLevel, pBlockPos)) sendPacketToPlayer(player, pPacket);
	}
}
