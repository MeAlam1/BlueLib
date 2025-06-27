/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.variants;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.Nullable;

public record Variant(
		@Nullable JsonArray parameters
) {
	public static JsonDeserializer<Variant> deserializer() throws JsonParseException {
		return (json, type, context) -> {

			JsonArray parameters = json.getAsJsonArray();

			return new Variant(
					parameters
			);
		};
	}
}
