/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.deserializer.range;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public record ByteRange(
		@NotNull Byte min,
		@NotNull Byte max) {

	@NotNull
	public static JsonDeserializer<ByteRange> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Byte min = GsonHelper.getAsByte(obj, "min", (byte) 0);
			Byte max = GsonHelper.getAsByte(obj, "max", (byte) 100);

			return new ByteRange(
					min,
					max);
		};
	}
}
