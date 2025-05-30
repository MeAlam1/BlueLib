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

public record StopTriggeredEntityAnimPacket(int entityId, boolean isReplacedEntity, String controllerName, String animName) implements NetworkPacket<StopTriggeredEntityAnimPacket> {

    public static final ResourceLocation ID = BlueLibCommon.Resource.resource("stop_triggered_entity_anim");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.entityId);
        pBuffer.writeBoolean(this.isReplacedEntity);
        pBuffer.writeUtf(this.controllerName);
        pBuffer.writeUtf(this.animName);
    }

    public static StopTriggeredEntityAnimPacket decode(FriendlyByteBuf pBuffer) {
        int entityId = pBuffer.readVarInt();
        boolean isReplacedEntity = pBuffer.readBoolean();
        String controllerName = pBuffer.readUtf();
        String animName = pBuffer.readUtf();
        return new StopTriggeredEntityAnimPacket(entityId, isReplacedEntity, controllerName, animName);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
