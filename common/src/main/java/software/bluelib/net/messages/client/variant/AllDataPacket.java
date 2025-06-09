/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.variant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.Resource;

public record AllDataPacket(Map<String, JsonObject> allData) implements NetworkPacket<AllDataPacket> {

    public static final ResourceLocation ID = Resource.resource("all_data_packet");

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer) {
        pBuffer.writeMap(allData, FriendlyByteBuf::writeUtf, (buf, json) -> buf.writeUtf(json.toString()));
    }

    public static AllDataPacket decode(FriendlyByteBuf pBuffer) {
        Map<String, JsonObject> map = pBuffer.readMap(
                FriendlyByteBuf::readUtf,
                buf -> JsonParser.parseString(buf.readUtf()).getAsJsonObject());
        return new AllDataPacket(map);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
