/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.deserialize;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

import java.util.List;

public record TextureMesh(
		List<Float> localPivot,
		List<Float> position,
		List<Float> rotation,
		List<Float> scale,
		@Nullable String texture
) {
	public static JsonDeserializer<TextureMesh> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			List<Float> pivot = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "local_pivot", null));
			List<Float> position = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "position", null));
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "rotation", null));
			List<Float> scale = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "scale", null));
			String texture = GsonHelper.getAsString(obj, "texture", null);

			return new TextureMesh(
					pivot,
					position,
					rotation,
					scale,
					texture
			);
		};
	}
}
