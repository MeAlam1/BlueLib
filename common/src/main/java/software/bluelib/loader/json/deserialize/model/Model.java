/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import software.bluelib.api.utils.JsonUtils;

public record Model(
		String formatVersion,
		List<ModelGeometry> ModelGeometry) {

	public static JsonDeserializer<Model> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = GsonHelper.getAsString(obj, "format_version");
			List<ModelGeometry> ModelGeometry = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "minecraft:geometry", new JsonArray(0)), context, ModelGeometry.class);

			return new Model(
					formatVersion,
					ModelGeometry);
		};
	}
}
