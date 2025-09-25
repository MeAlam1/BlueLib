/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.loader.JsonUtils;

public record ControllerDeserializer(
		@NotNull String formatVersion,
		@NotNull List<GroupDeserializer> groupDeserializers) {

	@NotNull
	public static JsonDeserializer<ControllerDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = GsonHelper.getAsString(obj, "format_version", "1.0.0");
			List<GroupDeserializer> groupDeserializers = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "groups", new JsonArray(0)), context, GroupDeserializer.class);

			return new ControllerDeserializer(
					formatVersion,
					groupDeserializers);
		};
	}
}
