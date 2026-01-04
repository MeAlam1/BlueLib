/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.*;
import software.bluelib.client.net.data.DataRegistrySyncPacketHandler;

public class NeoForgeNetworkManager implements NetworkManager {

	@NotNull
	public static final String PROTOCOL_VERSION = "1.0.0";

	public static void registerServerMessages(@NotNull RegisterPayloadHandlersEvent pEvent) {
		var registrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION);

		var serverView = NetworkRegistry.getServerProvider();

		Set<ResourceLocation> serverS2CIds = new HashSet<>();
		serverView.forEach(info -> {
			if (info.getHandler() instanceof ClientNetworkPacketHandler<?>) {
				serverS2CIds.add(info.getId());
			}
		});

		serverView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> !(it.info().getHandler() instanceof ServerNetworkPacketHandler<?>))
				.forEach(it -> {
					if (!serverS2CIds.contains(it.info().getId())) {
						it.registerTypeToClient(registrar);
					}
				});

		serverView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> it.info().getHandler() instanceof ServerNetworkPacketHandler<?>)
				.forEach(it -> it.registerToServer(registrar));
	}

	public static void registerClientMessages(@NotNull RegisterPayloadHandlersEvent pEvent) {
		var registrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION)
				.optional();

		var netRegistrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION)
				.executesOn(HandlerThread.NETWORK)
				.optional();

		var clientView = NetworkRegistry.getClientProvider();

		Set<ResourceLocation> clientS2CIds = new HashSet<>();
		clientView.forEach(info -> {
			if (info.getHandler() instanceof ClientNetworkPacketHandler<?>) {
				clientS2CIds.add(info.getId());
			}
		});

		clientView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> !(it.info().getHandler() instanceof ClientNetworkPacketHandler<?>))
				.forEach(it -> {
					if (!clientS2CIds.contains(it.info().getId())) {
						it.registerTypeToServer(registrar);
					}
				});

		clientView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> it.info().getHandler() instanceof ClientNetworkPacketHandler<?>)
				.forEach(it -> {
					boolean handleAsync = it.info().getHandler() instanceof DataRegistrySyncPacketHandler<?, ?>;
					it.registerToClient(handleAsync ? netRegistrar : registrar);
				});
	}

	@Override
	public void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
		pPlayer.connection.send(pPacket);
	}

	@Override
	public void sendToServer(@NotNull NetworkPacket<?> pPacket) {
		Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingEntity(@NotNull Entity pTrackingEntity, @NotNull NetworkPacket<?> pPacket) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(pTrackingEntity, pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingBlock(@NotNull ServerLevel pLevel, @NotNull BlockPos pBlockPos, @NotNull NetworkPacket<?> pPacket) {
		PacketDistributor.sendToPlayersTrackingChunk(pLevel, new ChunkPos(pBlockPos), pPacket);
	}
}
