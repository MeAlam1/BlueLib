/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.deserializer.pos;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public record Vector3(
		@NotNull Float x,
		@NotNull Float y,
		@NotNull Float z) {

	@NotNull
	public static JsonDeserializer<Vector3> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Float x = GsonHelper.getAsFloat(obj, "x", 0);
			Float y = GsonHelper.getAsFloat(obj, "y", 0);
			Float z = GsonHelper.getAsFloat(obj, "z", 0);

			return new Vector3(
					x,
					y,
					z);
		};
	}
}
