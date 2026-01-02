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

	@Nullable
	private static List<PacketRegisterInfo<?>> c2s = null;
	@Nullable
	private static List<PacketRegisterInfo<?>> s2c = null;
	@Nullable
	private static List<PacketRegisterInfo<?>> all = null;

	public static void registerPacketProvider(@NotNull PacketProvider pProvider) {
		providers.add(pProvider);
		c2s = null;
		s2c = null;
		all = null;
	}

	@NotNull
	public static List<PacketRegisterInfo<?>> getC2SPayloads() {
		if (c2s == null) c2s = generate(PacketSide.C2S);
		return c2s;
	}

	@NotNull
	public static List<PacketRegisterInfo<?>> getS2CPayloads() {
		if (s2c == null) s2c = generate(PacketSide.S2C);
		return s2c;
	}

	@NotNull
	public static List<PacketRegisterInfo<?>> getAllPayloads() {
		if (all == null) {
			List<PacketRegisterInfo<?>> out = new ArrayList<>();
			out.addAll(getC2SPayloads());
			out.addAll(getS2CPayloads());
			all = out;
		}
		return all;
	}

	@NotNull
	private static List<PacketRegisterInfo<?>> generate(@NotNull PacketSide side) {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();
		for (PacketProvider p : providers) {
			list.addAll(side == PacketSide.C2S ? p.getC2SPackets() : p.getS2CPackets());
		}
		return list;
	}
}
