package software.bluelib.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;

import java.util.HashSet;
import java.util.Set;

public class FabricPacketInfo<T extends NetworkPacket<T>> {

	@NotNull
	private final PacketRegisterInfo<T> info;

	@NotNull
	private static final Set<ResourceLocation> REGISTERED_CLIENT_PAYLOADS = new HashSet<>();
	@NotNull
	private static final Set<ResourceLocation> REGISTERED_SERVER_PAYLOADS = new HashSet<>();

	public FabricPacketInfo(@NotNull PacketRegisterInfo<T> pInfo) {
		this.info = pInfo;
	}

	public static <T extends NetworkPacket<T>> void registerPacket(@NotNull PacketRegisterInfo<T> pInfo, @NotNull Boolean pClient) {
		PayloadTypeRegistry<RegistryFriendlyByteBuf> registry = pClient ? PayloadTypeRegistry.playS2C() : PayloadTypeRegistry.playC2S();
		registry.register(pInfo.getPayloadId(), pInfo.getCodec());
	}

	public void registerClientHandler() {
		if (!REGISTERED_CLIENT_PAYLOADS.add(info.getId())) {
			return;
		}
		ClientPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, ignored) -> {
			ClientNetworkPacketHandler<T> handler = (ClientNetworkPacketHandler<T>) info.getHandler();
			handler.handle(obj, Minecraft.getInstance());
		});
	}

	public void registerServerHandler() {
		if (!REGISTERED_SERVER_PAYLOADS.add(info.getId())) {
			return;
		}
		ServerPlayNetworking.registerGlobalReceiver(info.getPayloadId(), (obj, context) -> {
			ServerNetworkPacketHandler<T> handler = (ServerNetworkPacketHandler<T>) info.getHandler();
			handler.handle(obj, context.player().server, context.player());
		});
	}
}