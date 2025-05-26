package software.bluelib.loader.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.packet.MultiloaderPacket;
import software.bluelib.loader.service.GeckoLibNetworking;

import java.util.function.Consumer;


public class GeckoLibNetworkingNeoForge implements GeckoLibNetworking {
    private static PayloadRegistrar registrar = null;

    public static void init(IEventBus modBus) {
        modBus.addListener((Consumer<RegisterPayloadHandlersEvent>) event -> {
            registrar = event.registrar(BlueLibConstants.MOD_ID);
            GeckoLibNetworking.init();
            registrar = null;
        });
    }

    
    @Override
    public <B extends FriendlyByteBuf, P extends MultiloaderPacket> void registerPacketInternal(CustomPacketPayload.Type<P> payloadType, StreamCodec<B, P> codec, boolean isClientBound) {
        if (isClientBound) {
            registrar.playToClient(payloadType, (StreamCodec<FriendlyByteBuf, P>)codec, (packet, context) -> packet.receiveMessage(context.player(), context::enqueueWork));
        }
        else {
            registrar.playToServer(payloadType, (StreamCodec<FriendlyByteBuf, P>)codec, (packet, context) -> packet.receiveMessage(context.player(), context::enqueueWork));
        }
    }

    
    @Override
    public void sendToAllPlayersTrackingEntity(MultiloaderPacket packet, Entity trackingEntity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(trackingEntity, packet);
    }

    
    @Override
    public void sendToAllPlayersTrackingBlock(MultiloaderPacket packet, ServerLevel level, BlockPos pos) {
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), packet);
    }

    
    @Override
    public void sendToPlayer(MultiloaderPacket packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}
