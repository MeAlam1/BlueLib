/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

import java.util.List;

public record Controller(
		@Nullable String formatVersion,
		List<Group> groups) {

	public static JsonDeserializer<Controller> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = JsonUtils.getOptionalString(obj, "format_version");
			List<Group> groups = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "groups", new JsonArray(0)), context, Group.class);

			return new Controller(
					formatVersion,
					groups);
		};
	}
}
