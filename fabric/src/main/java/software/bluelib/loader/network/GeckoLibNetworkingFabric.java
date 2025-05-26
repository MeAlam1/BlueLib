package software.bluelib.loader.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.packet.MultiloaderPacket;
import software.bluelib.loader.service.GeckoLibNetworking;


public final class GeckoLibNetworkingFabric implements GeckoLibNetworking {
    
    @Override
    public <B extends FriendlyByteBuf, P extends MultiloaderPacket> void registerPacketInternal(CustomPacketPayload.Type<P> payloadType, StreamCodec<B, P> codec, boolean isClientBound) {
        if (isClientBound) {
            PayloadTypeRegistry.playS2C().register(payloadType, (StreamCodec<FriendlyByteBuf, P>)codec);

            if (BlueLibConstants.PlatformHelper.PLATFORM.isPhysicalClient())
                GeckoLibClient.registerPacket(payloadType);
        }
        else {
            PayloadTypeRegistry.playC2S().register(payloadType, (StreamCodec<FriendlyByteBuf, P>)codec);
            ServerPlayNetworking.registerGlobalReceiver(payloadType, (packet, context) -> packet.receiveMessage(context.player(), context.player().getServer()::execute));
        }
    }

    
    @Override
    public void sendToAllPlayersTrackingEntity(MultiloaderPacket packet, Entity trackingEntity) {
        if (trackingEntity instanceof ServerPlayer pl)
            sendToPlayer(packet, pl);

        for (ServerPlayer player : PlayerLookup.tracking(trackingEntity)) {
            sendToPlayer(packet, player);
        }
    }

    
    @Override
    public void sendToAllPlayersTrackingBlock(MultiloaderPacket packet, ServerLevel level, BlockPos pos) {
        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            sendToPlayer(packet, player);
        }
    }

    
    @Override
    public void sendToPlayer(MultiloaderPacket packet, ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }
}
