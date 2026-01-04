/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;

public record NeoForgePacketInfo<T extends NetworkPacket<T>>(@NotNull PacketRegisterInfo<T> info) {

	private static final Logger LOGGER = LoggerFactory.getLogger("BlueLib\\-Network");

	@NotNull
	private static final Set<ResourceLocation> REGISTERED_CLIENT_HANDLERS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_SERVER_HANDLERS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_CLIENT_TYPES = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_SERVER_TYPES = new HashSet<>();

	public void registerToClient(@NotNull PayloadRegistrar pRegistrar) {
		if (info == null) {
			LOGGER.error("Skipping client handler registration: PacketRegisterInfo is null");
			return;
		}
		if (info.getId() == null || info.getPayloadId() == null || info.getCodec() == null) {
			LOGGER.error("Skipping client handler registration due to null metadata: id={} payloadId={} codec={}",
					info.getId(), info.getPayloadId(), info.getCodec());
			return;
		}

		final Object rawHandler = info.getHandler();
		if (!(rawHandler instanceof ClientNetworkPacketHandler<?>)) {
			LOGGER.warn("Ignoring client handler {} because handler is {} (expected ClientNetworkPacketHandler). Handler value={}",
					info.getId(),
					(rawHandler == null ? "null" : rawHandler.getClass().getName()),
					rawHandler);
			return;
		}

		if (!REGISTERED_CLIENT_HANDLERS.add(info.getId())) {
			LOGGER.warn("Client handler already registered, skipping: {}", info.getId());
			return;
		}

		@SuppressWarnings("unchecked")
		final ClientNetworkPacketHandler<T> clientHandler = (ClientNetworkPacketHandler<T>) rawHandler;

		IPayloadHandler<T> handler = (arg, unused) -> {
			try {
				clientHandler.handle(arg, Minecraft.getInstance());
			} catch (Throwable t) {
				LOGGER.error("Exception while handling client payload {}", info.getId(), t);
				throw t;
			}
		};

		LOGGER.info("Registering client handler payload: {}", info.getId());
		pRegistrar.playToClient(info.getPayloadId(), info.getCodec(), handler);
	}

	public void registerToServer(@NotNull PayloadRegistrar pRegistrar) {
		if (info == null) {
			LOGGER.error("Skipping server handler registration: PacketRegisterInfo is null");
			return;
		}
		if (info.getId() == null || info.getPayloadId() == null || info.getCodec() == null) {
			LOGGER.error("Skipping server handler registration due to null metadata: id={} payloadId={} codec={}",
					info.getId(), info.getPayloadId(), info.getCodec());
			return;
		}

		final Object rawHandler = info.getHandler();
		if (!(rawHandler instanceof ServerNetworkPacketHandler<?>)) {
			LOGGER.error("Skipping server handler {} because handler is {} (expected ServerNetworkPacketHandler). Handler value={}",
					info.getId(),
					(rawHandler == null ? "null" : rawHandler.getClass().getName()),
					rawHandler);
			return;
		}

		if (!REGISTERED_SERVER_HANDLERS.add(info.getId())) {
			LOGGER.warn("Server handler already registered, skipping: {}", info.getId());
			return;
		}

		@SuppressWarnings("unchecked")
		final ServerNetworkPacketHandler<T> serverHandler = (ServerNetworkPacketHandler<T>) rawHandler;

		IPayloadHandler<T> handler = (arg, ctx) -> {
			try {
				serverHandler.handle(arg, ctx.player().getServer(), (ServerPlayer) ctx.player());
			} catch (Throwable t) {
				LOGGER.error("Exception while handling server payload {}", info.getId(), t);
				throw t;
			}
		};

		LOGGER.info("Registering server handler payload: {}", info.getId());
		pRegistrar.playToServer(info.getPayloadId(), info.getCodec(), handler);
	}

	/**
	 * Client "payload only" registration for ids that have no client handler.
	 * These are S2C identifiers in the client view, so they must be CLIENTBOUND.
	 */
	public void registerTypeToClient(@NotNull PayloadRegistrar pRegistrar) {
		if (info == null) {
			LOGGER.error("Skipping client type registration: PacketRegisterInfo is null");
			return;
		}
		if (info.getId() == null || info.getPayloadId() == null || info.getCodec() == null) {
			LOGGER.error("Skipping client type registration due to null metadata: id={} payloadId={} codec={}",
					info.getId(), info.getPayloadId(), info.getCodec());
			return;
		}

		if (!REGISTERED_CLIENT_TYPES.add(info.getId())) {
			LOGGER.warn("Client payload type already registered, skipping: {}", info.getId());
			return;
		}

		LOGGER.info("Registering client payload type (S2C): {}", info.getId());
		pRegistrar.playToClient(info.getPayloadId(), info.getCodec(), (arg, ctx) -> {
			LOGGER.warn("Client received payload {} without a registered handler", info.getId());
		});
	}

	public void registerTypeToServer(@NotNull PayloadRegistrar pRegistrar) {
		if (info == null) {
			LOGGER.error("Skipping server type registration: PacketRegisterInfo is null");
			return;
		}
		if (info.getId() == null || info.getPayloadId() == null || info.getCodec() == null) {
			LOGGER.error("Skipping server type registration due to null metadata: id={} payloadId={} codec={}",
					info.getId(), info.getPayloadId(), info.getCodec());
			return;
		}

		if (!REGISTERED_SERVER_TYPES.add(info.getId())) {
			LOGGER.warn("Server payload type already registered, skipping: {}", info.getId());
			return;
		}

		LOGGER.info("Registering server payload type: {}", info.getId());
		pRegistrar.playToServer(info.getPayloadId(), info.getCodec(), (arg, ctx) -> {
			LOGGER.warn("Server received payload {} without a registered handler", info.getId());
		});
	}
}
