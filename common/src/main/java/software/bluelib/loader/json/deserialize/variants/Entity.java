/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.variants;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.Map;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public record Entity(
		@NotNull String formatVersion,
		@NotNull Map<String, Variant> variants) {

	@NotNull
	public static JsonDeserializer<Entity> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = GsonHelper.getAsString(obj, "format_version", "1.0.0");
			Map<String, Variant> variants = new java.util.HashMap<>();
			for (String variantName : obj.keySet()) {
				variants.put(variantName, context.deserialize(obj.get(variantName), Variant.class));
			}
			return new Entity(
					formatVersion,
					variants);

		};
	}
}
