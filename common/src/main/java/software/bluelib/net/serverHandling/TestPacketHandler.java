/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.serverHandling;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.ServerNetworkPacketHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.net.messages.server.TestPacket;

/**
 * {@link TestPacket}
 */
public class TestPacketHandler implements ServerNetworkPacketHandler<TestPacket> {

    @Override
    public void handle(TestPacket pPacket, MinecraftServer pServer, ServerPlayer pPlayer) {
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("test", pPacket.value()), true);
    }
}
