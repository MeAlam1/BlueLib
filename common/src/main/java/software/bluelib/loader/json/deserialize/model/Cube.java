/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record Cube(
		List<Float> origin,
		List<Float> size,
		List<Float> pivot,
		List<Float> rotation,
		UVUnion uvUnion,
		@Nullable Float inflate,
		@Nullable Boolean mirror) {

	public static JsonDeserializer<Cube> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Float inflate = JsonUtils.getOptionalFloat(obj, "inflate");
			Boolean mirror = JsonUtils.getOptionalBoolean(obj, "mirror");

			List<Float> origin = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "origin"));
			List<Float> size = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "size"));
			List<Float> pivot = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "pivot"));
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "rotation"));
			UVUnion uvUnion = JsonUtils.getOptionalObject(obj, "uv", context, UVUnion.class);

			return new Cube(
					origin,
					size,
					pivot,
					rotation,
					uvUnion,
					inflate,
					mirror);
		};
	}
}
