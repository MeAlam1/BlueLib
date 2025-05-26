/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.deserialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import software.bluelib.client.loader.json.model.ModelFormatVersion;
import software.bluelib.api.utils.JsonUtils;

import java.util.List;

public record Model(
        ModelFormatVersion modelFormatVersion, 
        List<ModelGeometry> modelGeometry
) {
    public static JsonDeserializer<Model> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            JsonObject obj = json.getAsJsonObject();
            ModelFormatVersion modelFormatVersion = context.deserialize(obj.get("format_version"), ModelFormatVersion.class);
            List<ModelGeometry> modelGeometry = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "minecraft:geometry", new JsonArray(0)), context, ModelGeometry.class);

            return new Model(
                    modelFormatVersion, 
                    modelGeometry
            );
        };
    }
}
