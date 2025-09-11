/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.deserializer.color;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public record RGBAColor(
		@NotNull Integer red,
		@NotNull Integer green,
		@NotNull Integer blue,
		@NotNull Integer alpha) {

	@NotNull
	public static JsonDeserializer<RGBAColor> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			int red = GsonHelper.getAsInt(obj, "red", 0);
			int green = GsonHelper.getAsInt(obj, "green", 0);
			int blue = GsonHelper.getAsInt(obj, "blue", 0);
			int alpha = GsonHelper.getAsInt(obj, "alpha", 255);

			return new RGBAColor(
					Math.max(0, Math.min(255, red)),
					Math.max(0, Math.min(255, green)),
					Math.max(0, Math.min(255, blue)),
					Math.max(0, Math.min(255, alpha)));
		};
	}
}
