package software.bluelib.net;

import java.util.HashSet;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.client.net.data.DataRegistrySyncPacketHandler;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class NeoForgeNetworkManager implements BlueLibConstants.NetworkManager {

    public static final String PROTOCOL_VERSION = "1.0.0";

    public static void registerMessages(RegisterPayloadHandlersEvent pEvent) {
        var registrar = pEvent.registrar(BlueLibConstants.MOD_ID).versioned(PROTOCOL_VERSION);

        var netRegistrar = pEvent.registrar(BlueLibConstants.MOD_ID)
                .versioned(PROTOCOL_VERSION)
                .executesOn(HandlerThread.NETWORK);

        var syncPackets = new HashSet<ResourceLocation>();
        var asyncPackets = new HashSet<ResourceLocation>();

        NetworkRegistry.s2cPayloads.stream()
                .map(NeoForgePacketInfo::new)
                .forEach(it -> {
                    boolean handleAsync = it.info().getHandler() instanceof DataRegistrySyncPacketHandler<?, ?>;
                    if (handleAsync) {
                        asyncPackets.add(it.info().getId());
                    } else {
                        syncPackets.add(it.info().getId());
                    }

                    it.registerToClient(handleAsync ? netRegistrar : registrar);
                });

        NetworkRegistry.c2sPayloads.stream()
                .map(NeoForgePacketInfo::new)
                .forEach(it -> {
                    it.registerToServer(registrar);
                });
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer pPlayer, NetworkPacket<?> pPacket) {
        try {
            pPlayer.connection.send(pPacket);
            BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.translate("packet.send.player.success", pPacket.getClass().getSimpleName(), pPlayer.getName().getString()), true);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.translate("packet.send.player.fail", pPacket.getClass().getSimpleName(), pPlayer.getName().getString()), pException, true);
        }
    }

    @Override
    public void sendToServer(NetworkPacket<?> pPacket) {
        try {
            Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(pPacket);
            BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.translate("packet.send.server.success", pPacket.getClass().getSimpleName()), true);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.translate("packet.send.server.fail", pPacket.getClass().getSimpleName()), pException, true);
        }
    }
}