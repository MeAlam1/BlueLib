/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.Resource;
import software.bluelib.net.serverHandling.TestPacketHandler;

/**
 * {@link TestPacketHandler}
 */
public record TestPacket(boolean value) implements NetworkPacket<TestPacket> {

    public static final ResourceLocation ID = Resource.resource("test_packet");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        pBuffer.writeBoolean(value);
    }

    public static TestPacket decode(RegistryFriendlyByteBuf pBuffer) {
        return new TestPacket(pBuffer.readBoolean());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
