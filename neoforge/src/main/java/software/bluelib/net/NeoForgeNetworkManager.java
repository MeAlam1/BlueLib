/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.client.net.data.DataRegistrySyncPacketHandler;

import java.util.HashSet;
import java.util.Objects;

public class NeoForgeNetworkManager implements NetworkManager {

	@NotNull
	public static final String PROTOCOL_VERSION = "1.0.0";

	public static void registerMessages(@NotNull RegisterPayloadHandlersEvent pEvent) {
		var registrar = pEvent.registrar(BlueLibConstants.MOD_ID).versioned(PROTOCOL_VERSION);

		var netRegistrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION)
				.executesOn(HandlerThread.NETWORK);

		var syncPackets = new HashSet<ResourceLocation>();
		var asyncPackets = new HashSet<ResourceLocation>();

		NetworkRegistry.s2cPayloads.stream()
				.map(NeoForgePacketInfo::new)
				.forEach(it -> {
					boolean handleAsync = it.info().getHandler() instanceof DataRegistrySyncPacketHandler<?, ?>;
					if (handleAsync) {
						asyncPackets.add(it.info().getId());
					} else {
						syncPackets.add(it.info().getId());
					}

					it.registerToClient(handleAsync ? netRegistrar : registrar);
				});

		NetworkRegistry.c2sPayloads.stream()
				.map(NeoForgePacketInfo::new)
				.forEach(it -> it.registerToServer(registrar));
	}

	@Override
	public void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
		pPlayer.connection.send(pPacket);
	}

	@Override
	public void sendToServer(@NotNull NetworkPacket<?> pPacket) {
		Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(pPacket);
	}
}
