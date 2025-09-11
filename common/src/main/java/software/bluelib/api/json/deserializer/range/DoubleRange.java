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

public record DoubleRange(
		@NotNull Double min,
		@NotNull Double max) {

	@NotNull
	public static JsonDeserializer<DoubleRange> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Double min = GsonHelper.getAsDouble(obj, "min", 0);
			Double max = GsonHelper.getAsDouble(obj, "max", 100);

			return new DoubleRange(
					min,
					max);
		};
	}
}
