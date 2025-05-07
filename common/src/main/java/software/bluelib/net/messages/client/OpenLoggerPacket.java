// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.net.messages.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.client.net.OpenLoggerPacketHandler;

/**
 * {@link OpenLoggerPacketHandler}
 */
public record OpenLoggerPacket() implements NetworkPacket<OpenLoggerPacket> {

    public static final ResourceLocation ID = BlueLibCommon.Resource.resource("open_screen_packet");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {}

    public static OpenLoggerPacket decode(FriendlyByteBuf pBuffer) {
        return new OpenLoggerPacket();
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
