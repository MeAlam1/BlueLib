/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import software.bluelib.api.utils.JsonUtils;
import software.bluelib.client.loader.cache.animations.AnimationLibraryCache;

public record AnimationLibrary(
		String formatVersion,
		AnimationLibraryCache animations) {

	public static JsonDeserializer<AnimationLibrary> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String formatVersion = GsonHelper.getAsString(obj, "format_version");
			AnimationLibraryCache animations = JsonUtils.getOptionalObject(obj, "animations", context, AnimationLibraryCache.class);

			return new AnimationLibrary(
					formatVersion,
					animations);

		};
	}
}
