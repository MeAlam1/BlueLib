package software.bluelib.net;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.Encodable;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.net.PacketHandler;
import software.bluelib.api.net.ServerNetworkPacketHandler;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber
public final class NeoForgePacketInfo {

	private static final Set<ResourceLocation> REGISTERED_PAYLOAD_IDS = new HashSet<>();

	@SubscribeEvent
	public static void registerPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("bluelib");

		for (PacketRegisterInfo<?> info : NetworkRegistry.getAllPayloads()) {
			ResourceLocation payloadId = info.getId();
			if (!REGISTERED_PAYLOAD_IDS.add(payloadId)) continue;

			registerBothDirections(registrar, info);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T extends NetworkPacket<T> & Encodable> void registerBothDirections(
			PayloadRegistrar registrar,
			PacketRegisterInfo<T> info
	) {
		PacketHandler<T> handler = info.getHandler();
		boolean isClient = FMLEnvironment.dist.isClient();

		registrar.playToClient(
				info.getPayloadId(),
				info.getCodec(),
				(payload, context) -> {
					if (!isClient) return;

					if (handler instanceof ClientNetworkPacketHandler<T> clientHandler) {
						context.enqueueWork(() -> clientHandler.handle(payload, Minecraft.getInstance()));
					}
				}
		);

		registrar.playToServer(
				info.getPayloadId(),
				info.getCodec(),
				(payload, context) -> {
					if (isClient) return;

					if (handler instanceof ServerNetworkPacketHandler<T> serverHandler) {
						context.enqueueWork(() ->
								serverHandler.handle(
										payload,
										context.player().getServer(),
										(ServerPlayer) context.player()
								)
						);
					}
				}
		);
	}
}