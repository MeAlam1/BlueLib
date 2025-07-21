/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.controller;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.JsonUtils;

public record Group(
		@NotNull Map<String, Behaviour> behaviours) {

	@NotNull
	public static JsonDeserializer<Group> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Map<String, Behaviour> behaviours = JsonUtils.jsonObjToMap(obj, context, Behaviour.class);

			return new Group(
					behaviours);
		};
	}
}
