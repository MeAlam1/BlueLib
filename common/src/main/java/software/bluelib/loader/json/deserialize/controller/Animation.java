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
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record Animation(
		@NotNull List<String> conditions,
		@NotNull String animation,
		@Nullable Integer priority,
		@Nullable String sound) {

	@NotNull
	public static JsonDeserializer<Animation> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<String> conditions = JsonUtils.jsonArrayToStringList(GsonHelper.getAsJsonArray(obj, "conditions"));
			String animation = GsonHelper.getAsString(obj, "animation");
			Integer priority = JsonUtils.getOptionalInteger(obj, "priority");
			String sound = JsonUtils.getOptionalString(obj, "sound");

			return new Animation(
					conditions,
					animation,
					priority,
					sound);
		};
	}
}
