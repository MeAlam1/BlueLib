/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.loader;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;

public record StopTriggeredEntityAnimPacket(
		@NotNull Integer entityId,
		boolean isReplacedEntity,
		@NotNull String controllerName,
		@NotNull String animName) implements NetworkPacket<StopTriggeredEntityAnimPacket> {

	public static final ResourceLocation ID = BlueResource.resource("stop_triggered_entity_anim");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeVarInt(this.entityId);
		pBuffer.writeBoolean(this.isReplacedEntity);
		pBuffer.writeUtf(this.controllerName);
		pBuffer.writeUtf(this.animName);
	}

	public static @NotNull StopTriggeredEntityAnimPacket decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		Integer entityId = pBuffer.readVarInt();
		boolean isReplacedEntity = pBuffer.readBoolean();
		String controllerName = pBuffer.readUtf();
		String animName = pBuffer.readUtf();
		return new StopTriggeredEntityAnimPacket(entityId, isReplacedEntity, controllerName, animName);
	}

	@Override
	@NotNull
	public ResourceLocation getId() {
		return ID;
	}
}
