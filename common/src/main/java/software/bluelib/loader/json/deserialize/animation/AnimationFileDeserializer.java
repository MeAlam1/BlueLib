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

import java.util.Map;

public record AnimationFileDeserializer(
		@NotNull String formatVersion,
		@NotNull AnimationsDeserializer animations) {

	@NotNull
	public static JsonDeserializer<AnimationFileDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = GsonHelper.getAsString(obj, "format_version");
			AnimationsDeserializer animations = GsonHelper.getAsObject(obj,"animations", context, AnimationsDeserializer.class);

			return new AnimationFileDeserializer(
					formatVersion,
					animations);

		};
	}
}
