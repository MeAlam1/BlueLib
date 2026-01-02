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
import software.bluelib.api.net.Encodable;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.PacketHandler;
import software.bluelib.api.net.ServerNetworkPacketHandler;

public record FabricPacketInfo<T extends NetworkPacket<T> & Encodable>(@NotNull PacketRegisterInfo<T> info) {

	@NotNull
	private static final Set<ResourceLocation> REGISTERED_S2C_PAYLOAD_TYPES = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_C2S_PAYLOAD_TYPES = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_S2C_HANDLERS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_C2S_HANDLERS = new HashSet<>();

	public static <T extends NetworkPacket<T> & Encodable> void registerS2CPayload(@NotNull PacketRegisterInfo<T> pInfo) {
		if (!REGISTERED_S2C_PAYLOAD_TYPES.add(pInfo.getId())) return;
		PayloadTypeRegistry.playS2C().register(pInfo.getPayloadId(), pInfo.getCodec());
	}

	public static <T extends NetworkPacket<T> & Encodable> void registerC2SPayload(@NotNull PacketRegisterInfo<T> pInfo) {
		if (!REGISTERED_C2S_PAYLOAD_TYPES.add(pInfo.getId())) return;
		PayloadTypeRegistry.playC2S().register(pInfo.getPayloadId(), pInfo.getCodec());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerClientHandlers(@NotNull List<PacketRegisterInfo<?>> pInfos) {
		for (PacketRegisterInfo<?> rawInfo : pInfos) {
			PacketHandler<?> handler = rawInfo.getHandler();
			if (!(handler instanceof ClientNetworkPacketHandler<?>)) continue;
			if (!REGISTERED_S2C_HANDLERS.add(rawInfo.getId())) continue;

			ClientNetworkPacketHandler clientHandler = (ClientNetworkPacketHandler) handler;

			ClientPlayNetworking.registerGlobalReceiver(((PacketRegisterInfo) rawInfo).getPayloadId(), (obj, ignored) -> {
				@NotNull
				Minecraft mc = Minecraft.getInstance();
				clientHandler.handle((NetworkPacket) obj, mc);
			});
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerServerHandlers(@NotNull List<PacketRegisterInfo<?>> pInfos) {
		for (PacketRegisterInfo<?> rawInfo : pInfos) {
			PacketHandler<?> handler = rawInfo.getHandler();
			if (!(handler instanceof ServerNetworkPacketHandler<?>)) continue;
			if (!REGISTERED_C2S_HANDLERS.add(rawInfo.getId())) continue;

			ServerNetworkPacketHandler serverHandler = (ServerNetworkPacketHandler) handler;

			ServerPlayNetworking.registerGlobalReceiver(((PacketRegisterInfo) rawInfo).getPayloadId(), (obj, context) -> {
				serverHandler.handle((NetworkPacket) obj, context.player().server, context.player());
			});
		}
	}
}
