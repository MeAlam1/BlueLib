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

public record StateDeserializer(
		boolean isOverlay,
		@NotNull List<ControllerAnimationDeserializer> controllerAnimationDeserializers) {

	@NotNull
	public static JsonDeserializer<StateDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			boolean isOverlay = Boolean.TRUE.equals(JsonUtils.getOptionalBoolean(obj, "is_overlay"));
			List<ControllerAnimationDeserializer> controllerAnimationDeserializers = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "animations"), context, ControllerAnimationDeserializer.class);

			return new StateDeserializer(
					isOverlay,
					controllerAnimationDeserializers);
		};
	}
}
