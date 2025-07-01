/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;

public record ParameterDataPacket(@NotNull JsonElement parameterData) implements NetworkPacket<ParameterDataPacket> {

    @NotNull
    public static final ResourceLocation ID = BlueResource.resource("parameter_data_packet");

    @Override
    public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(parameterData.toString());
    }

    @NotNull
    public static ParameterDataPacket decode(@NotNull FriendlyByteBuf pBuffer) {
        String json = pBuffer.readUtf();
        JsonElement element = JsonParser.parseString(json);
        return new ParameterDataPacket(element);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }
}
