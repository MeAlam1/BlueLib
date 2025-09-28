/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.json.deserialize.variants.VariantDeserializer;

import java.util.Map;

public record AnimationsDeserializer(
		@NotNull String formatVersion,
		@NotNull Map<String, AnimationDeserializer> animations) {

	@NotNull
	public static JsonDeserializer<AnimationsDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = GsonHelper.getAsString(obj, "format_version");
			Map<String, AnimationDeserializer> animations = new java.util.HashMap<>();
			for (String animationName : obj.keySet()) {
				animations.put(animationName, context.deserialize(obj.get(animationName), AnimationDeserializer.class));
			}

			return new AnimationsDeserializer(
					formatVersion,
					animations);

		};
	}
}
