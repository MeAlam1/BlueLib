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
import software.bluelib.api.utils.loader.JsonUtils;

public record State(
		boolean isOverlay,
		@NotNull List<Animation> animations) {

	@NotNull
	public static JsonDeserializer<State> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			boolean isOverlay = Boolean.TRUE.equals(JsonUtils.getOptionalBoolean(obj, "is_overlay"));
			List<Animation> animations = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "animations"), context, Animation.class);

			return new State(
					isOverlay,
					animations);
		};
	}
}
