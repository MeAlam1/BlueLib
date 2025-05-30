/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.registry;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.net.PacketProvider;
import software.bluelib.client.net.OpenLoggerPacketHandler;
import software.bluelib.client.net.loader.*;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.client.loader.*;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

@ApiStatus.Internal
public class BlueNetworkRegistry implements PacketProvider.C2SPacketProvider, PacketProvider.S2CPacketProvider {

    @Override
    public List<PacketRegisterInfo<?>> getC2SPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        // Test
        list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler()));

        return list;
    }

    @Override
    public List<PacketRegisterInfo<?>> getS2CPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        // Logger
        list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode, new OpenLoggerPacketHandler()));

        // Loader
        list.add(new PacketRegisterInfo<>(BlockEntityAnimTriggerPacket.ID, BlockEntityAnimTriggerPacket::decode, new BlockEntityAnimTriggerPacketHandler()));
        list.add(new PacketRegisterInfo<>(BlockEntityDataSyncPacket.ID, BlockEntityDataSyncPacket::decode, new BlockEntityDataSyncPacketHandler<>()));
        list.add(new PacketRegisterInfo<>(EntityAnimTriggerPacket.ID, EntityAnimTriggerPacket::decode, new EntityAnimTriggerPacketHandler()));
        list.add(new PacketRegisterInfo<>(EntityDataSyncPacket.ID, EntityDataSyncPacket::decode, new EntityDataSyncPacketHandler<>()));
        list.add(new PacketRegisterInfo<>(SingletonAnimTriggerPacket.ID, SingletonAnimTriggerPacket::decode, new SingletonAnimTriggerPacketHandler()));
        list.add(new PacketRegisterInfo<>(SingletonDataSyncPacket.ID, SingletonDataSyncPacket::decode, new SingletonDataSyncPacketHandler<>()));
        list.add(new PacketRegisterInfo<>(StopTriggeredEntityAnimPacket.ID, StopTriggeredEntityAnimPacket::decode, new StopTriggeredEntityAnimPacketHandler()));
        list.add(new PacketRegisterInfo<>(StopTriggeredBlockEntityAnimPacket.ID, StopTriggeredBlockEntityAnimPacket::decode, new StopTriggeredBlockEntityAnimPacketHandler()));
        list.add(new PacketRegisterInfo<>(StopTriggeredSingletonAnimPacket.ID, StopTriggeredSingletonAnimPacket::decode, new StopTriggeredSingletonAnimPacketHandler()));

        return list;
    }
}
