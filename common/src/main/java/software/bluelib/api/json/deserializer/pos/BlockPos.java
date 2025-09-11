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

public record BlockPos(
		@NotNull Integer x,
		@NotNull Integer y,
		@NotNull Integer z) {

	@NotNull
	public static JsonDeserializer<BlockPos> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Integer x = GsonHelper.getAsInt(obj, "x", 0);
			Integer y = GsonHelper.getAsInt(obj, "y", 0);
			Integer z = GsonHelper.getAsInt(obj, "z", 0);

			return new BlockPos(
					x,
					y,
					z);
		};
	}
}
