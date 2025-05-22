/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net;

import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.client.gui.logging.LoggerScreen;
import software.bluelib.net.messages.client.OpenLoggerPacket;

public class OpenLoggerPacketHandler implements ClientNetworkPacketHandler<OpenLoggerPacket> {

    @Override
    public void handle(OpenLoggerPacket pPacket, Minecraft pClient) {
        pClient.execute(() -> {
            pClient.setScreen(new LoggerScreen());
        });
    }
}
