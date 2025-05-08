// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.net.messages.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

/**
 * {@link TestPacketHandler}
 */
public record TestPacket(boolean value) implements NetworkPacket<TestPacket> {

    public static final ResourceLocation ID = BlueLibCommon.Resource.resource("test_packet");

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
