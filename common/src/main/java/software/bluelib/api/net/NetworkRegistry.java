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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.net.PacketRegisterInfo;

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

	public static void sendToAllPlayers(@NotNull NetworkPacket<?> pPacket) {
		MinecraftServer server = BlueLibConstants.PlatformHelper.PLATFORM.getServer();
		if (server == null) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.translate("server.null"));
			return;
		}
		sendPacketToPlayers(server.getPlayerList().getPlayers(), pPacket);
	}

	public static void sendPacketToPlayers(@NotNull Iterable<ServerPlayer> pPlayers, @NotNull NetworkPacket<?> pPacket) {
		for (ServerPlayer player : pPlayers) {
			sendPacketToPlayer(player, pPacket);
		}
	}

	private static final List<PacketProvider.C2SPacketProvider> c2sProviders = new ArrayList<>();
	private static final List<PacketProvider.S2CPacketProvider> s2cProviders = new ArrayList<>();

	private static List<PacketRegisterInfo<?>> c2sPayloads = null;
	private static List<PacketRegisterInfo<?>> s2cPayloads = null;

	public static List<PacketRegisterInfo<?>> getC2SPayloads() {
		if (c2sPayloads == null) c2sPayloads = generateC2SPacketInfoList();
		return c2sPayloads;
	}

	public static List<PacketRegisterInfo<?>> getS2CPayloads() {
		if (s2cPayloads == null) s2cPayloads = generateS2CPacketInfoList();
		return s2cPayloads;
	}

	@NotNull
	private static List<PacketRegisterInfo<?>> generateS2CPacketInfoList() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();
		for (PacketProvider.S2CPacketProvider provider : s2cProviders) {
			list.addAll(provider.getS2CPacketInfoList());
		}
		return list;
	}

	@NotNull
	private static List<PacketRegisterInfo<?>> generateC2SPacketInfoList() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();
		for (PacketProvider.C2SPacketProvider provider : c2sProviders) {
			list.addAll(provider.getC2SPacketInfoList());
		}
		return list;
	}

	public static void registerC2SPacketProvider(@NotNull PacketProvider.C2SPacketProvider pProvider) {
		c2sProviders.add(pProvider);
		c2sPayloads = null;
	}

	public static void registerS2CPacketProvider(@NotNull PacketProvider.S2CPacketProvider pProvider) {
		s2cProviders.add(pProvider);
		s2cPayloads = null;
	}
}
