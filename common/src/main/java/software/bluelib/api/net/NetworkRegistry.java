// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.BlueLibConstants;
import software.bluelib.net.PacketRegisterInfo;

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

    private static final List<PacketProvider.C2SPacketProvider> c2sProviders = new ArrayList<>();
    private static final List<PacketProvider.S2CPacketProvider> s2cProviders = new ArrayList<>();

    public static List<PacketRegisterInfo<?>> s2cPayloads = generateS2CPacketInfoList();
    public static List<PacketRegisterInfo<?>> c2sPayloads = generateC2SPacketInfoList();

    private static List<PacketRegisterInfo<?>> generateS2CPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();
        for (PacketProvider.S2CPacketProvider provider : s2cProviders) {
            list.addAll(provider.getS2CPacketInfoList());
        }
        return list;
    }

    private static List<PacketRegisterInfo<?>> generateC2SPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();
        for (PacketProvider.C2SPacketProvider provider : c2sProviders) {
            list.addAll(provider.getC2SPacketInfoList());
        }
        return list;
    }

    public static void registerC2SPacketProvider(PacketProvider.C2SPacketProvider pProvider) {
        c2sProviders.add(pProvider);
        c2sPayloads = generateC2SPacketInfoList();
    }

    public static void registerS2CPacketProvider(PacketProvider.S2CPacketProvider pProvider) {
        s2cProviders.add(pProvider);
        s2cPayloads = generateS2CPacketInfoList();
    }
}
