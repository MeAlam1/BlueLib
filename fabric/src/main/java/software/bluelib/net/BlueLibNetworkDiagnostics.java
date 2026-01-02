/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import java.util.List;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerPlayerConnection;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public final class BlueLibNetworkDiagnostics {

	private BlueLibNetworkDiagnostics() {}

	public static void registerServer() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			logServerJoin(handler, "JOIN");
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			logServerJoin(handler, "DISCONNECT");
		});
	}

	public static void registerClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			logClientJoin("JOIN");
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			logClientJoin("DISCONNECT");
		});
	}

	private static void logClientJoin(@NotNull String phase) {
		String env = FabricLoader.getInstance().getEnvironmentType().name();
		List<PacketRegisterInfo<?>> s2c = NetworkRegistry.getS2CPayloads();
		List<PacketRegisterInfo<?>> c2s = NetworkRegistry.getC2SPayloads();

		BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("[BlueLib][NetDiag][CLIENT][" + phase + "] env=" + env));
		BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("[BlueLib][NetDiag][CLIENT] S2C payloads=" + s2c.size() + " ids=" + ids(s2c)));
		BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("[BlueLib][NetDiag][CLIENT] C2S payloads=" + c2s.size() + " ids=" + ids(c2s)));

		if (s2c.isEmpty()) {
			BaseLogger.log(true, BaseLogLevel.WARNING, Component.literal("[BlueLib][NetDiag][CLIENT] S2C list is empty; incoming S2C packets will be discarded."));
		}
		if (c2s.isEmpty()) {
			BaseLogger.log(true, BaseLogLevel.WARNING, Component.literal("[BlueLib][NetDiag][CLIENT] C2S list is empty; outgoing C2S packets may fail / be discarded on server."));
		}
	}

	private static void logServerJoin(@NotNull ServerPlayerConnection handler, @NotNull String phase) {
		List<PacketRegisterInfo<?>> s2c = NetworkRegistry.getS2CPayloads();
		List<PacketRegisterInfo<?>> c2s = NetworkRegistry.getC2SPayloads();

		BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("[BlueLib][NetDiag][SERVER][" + phase + "] player=" + handler.getPlayer().getGameProfile().getName()));
		BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("[BlueLib][NetDiag][SERVER] S2C payloads=" + s2c.size() + " ids=" + ids(s2c)));
		BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("[BlueLib][NetDiag][SERVER] C2S payloads=" + c2s.size() + " ids=" + ids(c2s)));

		if (c2s.isEmpty()) {
			BaseLogger.log(true, BaseLogLevel.WARNING, Component.literal("[BlueLib][NetDiag][SERVER] C2S list is empty; incoming C2S packets will be discarded."));
		}
		if (s2c.isEmpty()) {
			BaseLogger.log(true, BaseLogLevel.WARNING, Component.literal("[BlueLib][NetDiag][SERVER] S2C list is empty; outgoing S2C packets may be discarded on client."));
		}
	}

	@NotNull
	private static String ids(@NotNull List<PacketRegisterInfo<?>> infos) {
		return infos.stream()
				.map(PacketRegisterInfo::getId)
				.map(Object::toString)
				.sorted()
				.collect(Collectors.joining(","));
	}
}
