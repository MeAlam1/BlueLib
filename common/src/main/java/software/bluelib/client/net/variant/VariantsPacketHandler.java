/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.net.variant;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.net.messages.client.variant.VariantsPacket;

public class VariantsPacketHandler implements ClientNetworkPacketHandler<VariantsPacket> {

    private final Consumer<VariantsPacket> handlerFunction;

    public VariantsPacketHandler(Consumer<VariantsPacket> pHandlerFunction) {
        this.handlerFunction = pHandlerFunction;
    }

    @Override
    public void handle(VariantsPacket pPacket, Minecraft pClient) {
        handlerFunction.accept(pPacket);
    }
}
