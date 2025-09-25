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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;

public record CubeDeserializer(
		@NotNull List<Float> origin,
		@NotNull List<Float> size,
		@NotNull List<Float> pivot,
		@NotNull List<Float> rotation,
		@NotNull UVUnionDeserializer uvUnionDeserializer,
		@Nullable Float inflate,
		@Nullable Boolean mirror) {

	@NotNull
	public static JsonDeserializer<CubeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Float inflate = JsonUtils.getOptionalFloat(obj, "inflate");
			Boolean mirror = JsonUtils.getOptionalBoolean(obj, "mirror");

			List<Float> origin = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "origin"));
			List<Float> size = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "size"));
			List<Float> pivot = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "pivot"));
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "rotation"));
			UVUnionDeserializer uvUnionDeserializer = JsonUtils.getOptionalObject(obj, "uv", context, UVUnionDeserializer.class);

			return new CubeDeserializer(
					origin,
					size,
					pivot,
					rotation,
					uvUnionDeserializer,
					inflate,
					mirror);
		};
	}
}
