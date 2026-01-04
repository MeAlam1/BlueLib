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
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;

public record NeoForgePacketInfo<T extends NetworkPacket<T>>(@NotNull PacketRegisterInfo<T> info) {

	@NotNull
	private static final Set<ResourceLocation> REGISTERED_CLIENT_HANDLERS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_SERVER_HANDLERS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_CLIENT_TYPES = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_SERVER_TYPES = new HashSet<>();

	public void registerToClient(@NotNull PayloadRegistrar pRegistrar) {
		final Object rawHandler = info.getHandler();
		if (!(rawHandler instanceof ClientNetworkPacketHandler<?>)) {
			return;
		}

		if (!REGISTERED_CLIENT_HANDLERS.add(info.getId())) {
			return;
		}

		@SuppressWarnings("unchecked")
		final ClientNetworkPacketHandler<T> clientHandler = (ClientNetworkPacketHandler<T>) rawHandler;

		IPayloadHandler<T> handler = (arg, unused) -> clientHandler.handle(arg, Minecraft.getInstance());

		pRegistrar.playToClient(info.getPayloadId(), info.getCodec(), handler);
	}

	public void registerToServer(@NotNull PayloadRegistrar pRegistrar) {
		final Object rawHandler = info.getHandler();
		if (!(rawHandler instanceof ServerNetworkPacketHandler<?>)) {
			return;
		}

		if (!REGISTERED_SERVER_HANDLERS.add(info.getId())) {
			return;
		}

		@SuppressWarnings("unchecked")
		final ServerNetworkPacketHandler<T> serverHandler = (ServerNetworkPacketHandler<T>) rawHandler;

		IPayloadHandler<T> handler = (arg, ctx) -> serverHandler.handle(arg, ctx.player().getServer(), (ServerPlayer) ctx.player());

		pRegistrar.playToServer(info.getPayloadId(), info.getCodec(), handler);
	}

	public void registerTypeToClient(@NotNull PayloadRegistrar pRegistrar) {
		if (!REGISTERED_CLIENT_TYPES.add(info.getId())) {
			return;
		}

		pRegistrar.playToClient(info.getPayloadId(), info.getCodec(), (arg, ctx) -> {});
	}

	public void registerTypeToServer(@NotNull PayloadRegistrar pRegistrar) {
		if (!REGISTERED_SERVER_TYPES.add(info.getId())) {
			return;
		}

		pRegistrar.playToServer(info.getPayloadId(), info.getCodec(), (arg, ctx) -> {});
	}
}
