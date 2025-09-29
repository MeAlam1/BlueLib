/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation.keyframe;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public record SoundKeyframeDeserializer(
		double startTick,
		@NotNull String sound) {

	@NotNull
	public static JsonDeserializer<SoundKeyframeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			double startTick = GsonHelper.getAsDouble(obj, "startTick");
			String sound = GsonHelper.getAsString(obj, "sound");

			return new SoundKeyframeDeserializer(
					startTick,
					sound);
		};
	}
}
