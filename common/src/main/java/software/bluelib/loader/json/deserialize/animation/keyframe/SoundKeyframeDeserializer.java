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

// TODO: Improve the Error Handling
public record SoundKeyframeDeserializer(
		double startTick,
		@NotNull String sound) {

	@NotNull
	public static JsonDeserializer<SoundKeyframeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			for (String key : obj.keySet()) {
				double startTick = Double.parseDouble(key);
				JsonObject soundObj = obj.getAsJsonObject(key);
				String sound = GsonHelper.getAsString(soundObj, "effect");

				return new SoundKeyframeDeserializer(
						startTick,
						sound);
			}
			return new SoundKeyframeDeserializer(0, "");
		};
	}
}
