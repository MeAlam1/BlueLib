/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;

public record FabricPacketInfo<T extends NetworkPacket<T>>(@NotNull PacketRegisterInfo<T> info) {

	@NotNull
	private static final Set<ResourceLocation> REGISTERED_S2C_PAYLOAD_TYPES = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_C2S_PAYLOAD_TYPES = new HashSet<>();

	@NotNull
	private static final Set<ResourceLocation> REGISTERED_CLIENT_RECEIVERS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_SERVER_RECEIVERS = new HashSet<>();

	/**
	 * Registers an S2C payload type (server sends, client receives).
	 */
	public static <T extends NetworkPacket<T>> void registerS2CPayload(@NotNull PacketRegisterInfo<T> pInfo) {
		if (!REGISTERED_S2C_PAYLOAD_TYPES.add(pInfo.getId())) return;
		PayloadTypeRegistry.playS2C().register(pInfo.getPayloadId(), pInfo.getCodec());
	}

	/**
	 * Registers a C2S payload type (client sends, server receives).
	 */
	public static <T extends NetworkPacket<T>> void registerC2SPayload(@NotNull PacketRegisterInfo<T> pInfo) {
		if (!REGISTERED_C2S_PAYLOAD_TYPES.add(pInfo.getId())) return;
		PayloadTypeRegistry.playC2S().register(pInfo.getPayloadId(), pInfo.getCodec());
	}

	public void registerClientHandler() {
		if (!REGISTERED_CLIENT_RECEIVERS.add(info.getId())) return;

		ClientPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, ignored) -> {
			ClientNetworkPacketHandler<T> handler = (ClientNetworkPacketHandler<T>) info.getHandler();
			handler.handle(obj, Minecraft.getInstance());
		});
	}

	public void registerServerHandler() {
		if (!REGISTERED_SERVER_RECEIVERS.add(info.getId())) return;

		ServerPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, context) -> {
			ServerNetworkPacketHandler<T> handler = (ServerNetworkPacketHandler<T>) info.getHandler();
			handler.handle(obj, context.player().server, context.player());
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends NetworkPacket<T>> void registerClientHandler(
			@NotNull List<PacketRegisterInfo<?>> infos,
			@NotNull ResourceLocation id,
			@NotNull Supplier<ClientNetworkPacketHandler<T>> handlerSupplier) {
		PacketRegisterInfo<T> info = (PacketRegisterInfo<T>) infos.stream()
				.filter(i -> i.getId().equals(id))
				.findFirst()
				.orElseThrow();

		ClientPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, ignored) -> {
			handlerSupplier.get().handle(obj, Minecraft.getInstance());
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends NetworkPacket<T>> void registerServerHandler(
			@NotNull List<PacketRegisterInfo<?>> infos,
			@NotNull ResourceLocation id,
			@NotNull Supplier<ServerNetworkPacketHandler<T>> handlerSupplier) {
		PacketRegisterInfo<T> info = (PacketRegisterInfo<T>) infos.stream()
				.filter(i -> i.getId().equals(id))
				.findFirst()
				.orElseThrow();

		ServerPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, context) -> {
			handlerSupplier.get().handle(obj, context.player().server, context.player());
		});
	}
}
