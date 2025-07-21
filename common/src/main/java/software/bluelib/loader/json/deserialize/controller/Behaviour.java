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
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.JsonUtils;

public record Behaviour(
		@NotNull Map<String, List<State>> states) {

	@NotNull
	public static JsonDeserializer<Behaviour> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Map<String, List<State>> states = JsonUtils.jsonObjToListMap(obj, context, State.class);

			return new Behaviour(
					states);
		};
	}
}
