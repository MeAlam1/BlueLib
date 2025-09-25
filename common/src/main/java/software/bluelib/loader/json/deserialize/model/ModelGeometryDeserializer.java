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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;

public record ModelGeometryDeserializer(
		@Nullable ModelDescriptionDeserializer modelDescriptionDeserializer,
		@NotNull List<BoneDeserializer> boneDeserializers,
		@Nullable String cape) {

	@NotNull
	public static JsonDeserializer<ModelGeometryDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			ModelDescriptionDeserializer modelDescriptionDeserializer = JsonUtils.getOptionalObject(obj, "description", context, ModelDescriptionDeserializer.class);
			List<BoneDeserializer> boneDeserializers = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "bones", new JsonArray(0)), context, BoneDeserializer.class);
			String cape = JsonUtils.getOptionalString(obj, "cape");

			return new ModelGeometryDeserializer(
					modelDescriptionDeserializer,
					boneDeserializers,
					cape);
		};
	}
}
