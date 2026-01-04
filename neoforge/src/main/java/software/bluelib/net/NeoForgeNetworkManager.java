package software.bluelib.net;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.api.net.NetworkPacket;

import java.util.Objects;

public class NeoForgeNetworkManager implements NetworkManager {

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