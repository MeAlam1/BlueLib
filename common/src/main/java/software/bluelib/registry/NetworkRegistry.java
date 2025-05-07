// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.client.net.OpenLoggerPacketHandler;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

public class NetworkRegistry {

    public static void sendPacket(ServerPlayer pPlayer, NetworkPacket<?> pPacket) {
        sendPacketToPlayer(pPlayer, pPacket);
    }

    public static void sendToServer(NetworkPacket<?> pPacket) {
        BlueLibConstants.PlatformHelper.REGISTRY.getNetwork().sendToServer(pPacket);
    }

    public static void sendPacketToPlayer(ServerPlayer pPlayer, NetworkPacket<?> pPacket) {
        BlueLibConstants.PlatformHelper.REGISTRY.getNetwork().sendPacketToPlayer(pPlayer, pPacket);
    }

    public static void sendToAllPlayers(NetworkPacket<?> pPacket) {
        sendPacketToPlayers(BlueLibConstants.PlatformHelper.PLATFORM.getServer().getPlayerList().getPlayers(), pPacket);
    }

    public static void sendPacketToPlayers(Iterable<ServerPlayer> pPlayers, NetworkPacket<?> pPacket) {
        for (ServerPlayer player : pPlayers) {
            sendPacketToPlayer(player, pPacket);
        }
    }

    public static List<PacketRegisterInfo<?>> s2cPayloads = generateS2CPacketInfoList();
    public static List<PacketRegisterInfo<?>> c2sPayloads = generatC2SPacketInfoList();

    private static List<PacketRegisterInfo<?>> generateS2CPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        // Logger
        list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode, new OpenLoggerPacketHandler(), null));

        return list;
    }

    private static List<PacketRegisterInfo<?>> generatC2SPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        // Test
        list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler(), null));

        return list;
    }
}
