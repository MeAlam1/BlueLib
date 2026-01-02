package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;

import java.util.HashSet;
import java.util.Set;

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
}