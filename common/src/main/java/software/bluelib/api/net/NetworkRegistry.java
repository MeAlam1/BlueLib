/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.net.PacketRegisterInfo;

@SuppressWarnings({ "unused" })
public class NetworkRegistry {

	public static void sendPacket(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
		sendPacketToPlayer(pPlayer, pPacket);
	}

	public static void sendToServer(@NotNull NetworkPacket<?> pPacket) {
		BlueLibConstants.PlatformHelper.REGISTRY.getNetwork().sendToServer(pPacket);
	}

	public static void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
		BlueLibConstants.PlatformHelper.REGISTRY.getNetwork().sendPacketToPlayer(pPlayer, pPacket);
	}

	public static void sendToAllPlayers(@NotNull MinecraftServer pServer, @NotNull NetworkPacket<?> pPacket) {
		sendPacketToPlayers(pServer.getPlayerList().getPlayers(), pPacket);
	}

	public static void sendPacketToPlayers(@NotNull Iterable<ServerPlayer> pPlayers, @NotNull NetworkPacket<?> pPacket) {
		for (ServerPlayer player : pPlayers) {
			sendPacketToPlayer(player, pPacket);
		}
	}

	public static void sendToAllPlayersTrackingEntity(@NotNull Entity pTrackingEntity, @NotNull NetworkPacket<?> pPacket) {
		BlueLibConstants.PlatformHelper.REGISTRY.getNetwork().sendToAllPlayersTrackingEntity(pTrackingEntity, pPacket);
	}

	public static void sendToAllPlayersTrackingBlock(@NotNull ServerLevel pLevel, @NotNull BlockPos pBlockPos, @NotNull NetworkPacket<?> pPacket) {
		BlueLibConstants.PlatformHelper.REGISTRY.getNetwork().sendToAllPlayersTrackingBlock(pLevel, pBlockPos, pPacket);
	}

	@NotNull
	private static final List<PacketProvider> providers = new ArrayList<>();

	/**
	 * CLIENT view:
	 * - S2C: decode + handler
	 * - C2S: decode only (identifiers, no handler)
	 */
	@Nullable
	private static List<PacketRegisterInfo<?>> clientProvider = null;

	/**
	 * SERVER/COMMON view:
	 * - C2S: decode + handler
	 * - S2C: decode only (identifiers, no handler)
	 */
	@Nullable
	private static List<PacketRegisterInfo<?>> serverProvider = null;

	public static void registerPacketProvider(@NotNull PacketProvider pProvider) {
		providers.add(pProvider);
		clientProvider = null;
		serverProvider = null;
	}

	@NotNull
	public static List<PacketRegisterInfo<?>> getClientProvider() {
		if (clientProvider == null) clientProvider = generateClientProvider();
		return clientProvider;
	}

	@NotNull
	public static List<PacketRegisterInfo<?>> getServerProvider() {
		if (serverProvider == null) serverProvider = generateServerProvider();
		return serverProvider;
	}

	@NotNull
	private static List<PacketRegisterInfo<?>> generateClientProvider() {
		List<PacketRegisterInfo<?>> out = new ArrayList<>();
		for (PacketProvider p : providers) {
			out.addAll(p.getS2CPackets());
			out.addAll(stripHandlers(p.getC2SPackets()));
		}
		return out;
	}

	@NotNull
	private static List<PacketRegisterInfo<?>> generateServerProvider() {
		List<PacketRegisterInfo<?>> out = new ArrayList<>();
		for (PacketProvider p : providers) {
			out.addAll(p.getC2SPackets());
			out.addAll(stripHandlers(p.getS2CPackets()));
		}
		return out;
	}

	@NotNull
	private static List<PacketRegisterInfo<?>> stripHandlers(@NotNull List<PacketRegisterInfo<?>> in) {
		List<PacketRegisterInfo<?>> out = new ArrayList<>(in.size());
		for (PacketRegisterInfo<?> info : in) {
			out.add(new PacketRegisterInfo<>(info.getId(), info.getDecoder()));
		}
		return out;
	}
}
