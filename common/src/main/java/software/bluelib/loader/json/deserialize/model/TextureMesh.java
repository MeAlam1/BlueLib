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

public record TextureMesh(
		@NotNull List<Float> localPivot,
		@NotNull List<Float> position,
		@NotNull List<Float> rotation,
		@NotNull List<Float> scale,
		@Nullable String texture) {

	@NotNull
	public static JsonDeserializer<TextureMesh> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			List<Float> pivot = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "local_pivot"));
			List<Float> position = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "position"));
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "rotation"));
			List<Float> scale = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "scale"));
			String texture = JsonUtils.getOptionalString(obj, "texture");

			return new TextureMesh(
					pivot,
					position,
					rotation,
					scale,
					texture);
		};
	}
}
