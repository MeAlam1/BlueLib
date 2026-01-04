package software.bluelib.net;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.client.net.data.DataRegistrySyncPacketHandler;

public class NeoForgeNetworkManager implements NetworkManager {

	private static final Logger LOGGER = LoggerFactory.getLogger("BlueLib\\-Network");

	@NotNull
	public static final String PROTOCOL_VERSION = "1.0.0";

	public static void registerServerMessages(@NotNull RegisterPayloadHandlersEvent pEvent) {
		LOGGER.info("Registering server payload handlers. modId={} protocol={}", BlueLibConstants.MOD_ID, PROTOCOL_VERSION);

		var registrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION);

		var serverView = NetworkRegistry.getServerProvider();
		LOGGER.info("Server provider count={}", serverView.size());

		serverView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> !(it.info().getHandler() instanceof software.bluelib.api.net.ServerNetworkPacketHandler<?>))
				.forEach(it -> it.registerTypeToServer(registrar));

		serverView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> it.info().getHandler() instanceof software.bluelib.api.net.ServerNetworkPacketHandler<?>)
				.forEach(it -> it.registerToServer(registrar));
	}

	public static void registerClientMessages(@NotNull RegisterPayloadHandlersEvent pEvent) {
		LOGGER.info("Registering client payload handlers. modId={} protocol={}", BlueLibConstants.MOD_ID, PROTOCOL_VERSION);

		var registrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION)
				.optional();

		var netRegistrar = pEvent.registrar(BlueLibConstants.MOD_ID)
				.versioned(PROTOCOL_VERSION)
				.executesOn(HandlerThread.NETWORK)
				.optional();

		var clientView = NetworkRegistry.getClientProvider();
		LOGGER.info("Client provider count={}", clientView.size());

		// ids that are S2C in the client view (have client handlers)
		Set<ResourceLocation> clientS2CIds = new HashSet<>();
		clientView.forEach(info -> {
			if (info.getHandler() instanceof ClientNetworkPacketHandler<?>) {
				clientS2CIds.add(info.getId());
			}
		});

		// Type-only registrations (no client handler):
		// Only register stripped C2S ids as SERVERBOUND on the client.
		// Do NOT register S2C types here, because registering the S2C handler already registers the payload type.
		clientView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> !(it.info().getHandler() instanceof ClientNetworkPacketHandler<?>))
				.forEach(it -> {
					if (!clientS2CIds.contains(it.info().getId())) {
						it.registerTypeToServer(registrar);
					}
				});

		// Handlers: S2C only (also registers the S2C payload type).
		clientView.stream()
				.map(NeoForgePacketInfo::new)
				.filter(it -> it.info().getHandler() instanceof ClientNetworkPacketHandler<?>)
				.forEach(it -> {
					boolean handleAsync = it.info().getHandler() instanceof DataRegistrySyncPacketHandler<?, ?>;
					it.registerToClient(handleAsync ? netRegistrar : registrar);
				});
	}

	@Override
	public void sendPacketToPlayer(@NotNull ServerPlayer pPlayer, @NotNull NetworkPacket<?> pPacket) {
		pPlayer.connection.send(pPacket);
	}

	@Override
	public void sendToServer(@NotNull NetworkPacket<?> pPacket) {
		Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingEntity(@NotNull Entity pTrackingEntity, @NotNull NetworkPacket<?> pPacket) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(pTrackingEntity, pPacket);
	}

	@Override
	public void sendToAllPlayersTrackingBlock(@NotNull ServerLevel pLevel, @NotNull BlockPos pBlockPos, @NotNull NetworkPacket<?> pPacket) {
		PacketDistributor.sendToPlayersTrackingChunk(pLevel, new ChunkPos(pBlockPos), pPacket);
	}
}