/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.loader;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;

public record SingletonAnimTriggerPacket(String syncableId, long instanceId, String controllerName,
        String animName) implements NetworkPacket<SingletonAnimTriggerPacket> {

    public static final ResourceLocation ID = BlueResource.resource("singleton_anim_trigger");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(this.syncableId);
        pBuffer.writeVarLong(this.instanceId);
        pBuffer.writeUtf(this.controllerName);
        pBuffer.writeUtf(this.animName);
    }

    public static SingletonAnimTriggerPacket decode(FriendlyByteBuf pBuffer) {
        String syncableId = pBuffer.readUtf();
        long instanceId = pBuffer.readVarLong();
        String controllerName = pBuffer.readUtf();
        String animName = pBuffer.readUtf();
        return new SingletonAnimTriggerPacket(syncableId, instanceId, controllerName, animName);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
