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
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record Behaviour(
		@NotNull List<String> conditions,
		@Nullable Integer priority,
		@NotNull Map<String, List<State>> states) {

	@NotNull
	public static JsonDeserializer<Behaviour> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<String> conditions = JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(obj, "conditions"));
			Integer priority = JsonUtils.getOptionalInteger(obj, "priority");
			JsonObject statesObj = JsonUtils.filterJsonObject(obj, "conditions", "priority");
			Map<String, List<State>> states = JsonUtils.jsonObjToListMap(statesObj, context, State.class);

			return new Behaviour(
					conditions,
					priority,
					states);
		};
	}
}
