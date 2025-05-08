package software.bluelib.net;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.ServerNetworkPacketHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public record NeoForgePacketInfo<T extends NetworkPacket<T>>(PacketRegisterInfo<T> info) {

    public void registerToClient(PayloadRegistrar pRegistrar) {
        try {
            IPayloadHandler<T> handler = (arg, unused) -> {
                ClientNetworkPacketHandler<T> clientHandler = (ClientNetworkPacketHandler<T>) info.getHandler();
                clientHandler.handle(arg, Minecraft.getInstance());
            };

            pRegistrar.playToClient(info.getPayloadId(), info.getCodec(), handler);
            BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.translate("packet.register.client.success", info.getPayloadId()), true);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.translate("packet.register.client.fail", info.getPayloadId()), pException, true);
        }
    }

    public void registerToServer(PayloadRegistrar pRegistrar) {
        try {
            IPayloadHandler<T> handler = (arg, ctx) -> {
                ServerNetworkPacketHandler<T> serverHandler = (ServerNetworkPacketHandler<T>) info.getHandler();
                serverHandler.handle(arg, ctx.player().getServer(), (ServerPlayer) ctx.player());
            };

            pRegistrar.playToServer(info.getPayloadId(), info.getCodec(), handler);
            BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.translate("packet.register.server.success", info.getPayloadId()), true);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.translate("packet.register.server.fail", info.getPayloadId()), pException, true);
        }
    }
}