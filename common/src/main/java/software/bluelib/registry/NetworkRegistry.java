// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.registry;

import software.bluelib.api.net.PacketProvider;
import software.bluelib.client.net.OpenLoggerPacketHandler;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class NetworkRegistry implements PacketProvider.C2SPacketProvider, PacketProvider.S2CPacketProvider {

    @Override
    public List<PacketRegisterInfo<?>> getC2SPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        // Test
        list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler(), null));

        return list;
    }

    @Override
    public List<PacketRegisterInfo<?>> getS2CPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        // Logger
        list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode, new OpenLoggerPacketHandler(), null));

        return list;
    }

}
