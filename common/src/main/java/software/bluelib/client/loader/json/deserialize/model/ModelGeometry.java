/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record ModelGeometry(
		@Nullable ModelDescription modelDescription,
		List<Bone> bones,
		@Nullable String cape) {

	public static JsonDeserializer<ModelGeometry> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			ModelDescription modelDescription = JsonUtils.getOptionalObject(obj, "description", context, ModelDescription.class);
			List<Bone> bones = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "bones", new JsonArray(0)), context, Bone.class);
			String cape = JsonUtils.getOptionalString(obj, "cape");

			return new ModelGeometry(
					modelDescription,
					bones,
					cape);
		};
	}
}
