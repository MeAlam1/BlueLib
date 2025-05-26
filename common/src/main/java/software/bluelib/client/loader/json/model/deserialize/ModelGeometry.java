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
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

import java.util.List;

public record ModelGeometry(
		@Nullable ModelDescription modelDescription,
		List<Bone> bones,
		@Nullable String cape
) {
	public static JsonDeserializer<ModelGeometry> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			ModelDescription modelDescription = GsonHelper.getAsObject(obj, "description", null, context, ModelDescription.class);
			List<Bone> bones = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "bones", new JsonArray(0)), context, Bone.class);
			String cape = GsonHelper.getAsString(obj, "cape", null);

			return new ModelGeometry(
					modelDescription,
					bones,
					cape
			);
		};
	}
}
