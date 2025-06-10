/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.Resource;

public record OpenLoggerPacket() implements NetworkPacket<OpenLoggerPacket> {

    public static final ResourceLocation ID = Resource.resource("open_screen_packet");

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
