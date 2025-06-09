/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.variant;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.Resource;

public record VariantsPacket(Set<String> allVariants) implements NetworkPacket<VariantsPacket> {

    public static final ResourceLocation ID = Resource.resource("variants_packet");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        pBuffer.writeCollection(allVariants, FriendlyByteBuf::writeUtf);
    }

    public static VariantsPacket decode(FriendlyByteBuf pBuffer) {
        Set<String> allVariants = pBuffer.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
        return new VariantsPacket(allVariants);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
