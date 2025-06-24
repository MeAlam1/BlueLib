/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

// TODO: Convert to Utils Please or Atleast Cleanup
public record UVUnion(
        List<Float> boxUVCoords,
        @Nullable UVFaces faceUV,
        boolean isBoxUV) {

    public static JsonDeserializer<UVUnion> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            if (json.isJsonObject()) {
                return new UVUnion(new ArrayList<>(), context.deserialize(json.getAsJsonObject(), UVFaces.class), false);
            } else if (json.isJsonArray()) {
                return new UVUnion(JsonUtils.jsonArrayToFloatList(json.getAsJsonArray()), null, true);
            } else {
                throw new JsonParseException("Invalid format provided for UVUnion, must be either double array or UVFaces collection");
            }
        };
    }
}
