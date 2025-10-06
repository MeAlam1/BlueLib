/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation.keyframe;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public record KeyframeDeserializer(
		@NotNull Map<String, KeyframeDataDeserializer> keyframeData) {

	@NotNull
	public static JsonDeserializer<KeyframeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Map<String, KeyframeDataDeserializer> keyframeData = new HashMap<>();
			for (String keyframeName : obj.keySet()) {
				JsonElement keyframeElement = obj.get(keyframeName);
				System.out.println("[KeyframeDeserializer] Keyframe: " + keyframeName + " | Type: " + (keyframeElement.isJsonArray() ? "Array" : keyframeElement.isJsonObject() ? "Object" : "Other") + " | Content: " + keyframeElement);
				keyframeData.put(keyframeName, context.deserialize(keyframeElement, KeyframeDataDeserializer.class));
			}

			return new KeyframeDeserializer(
					keyframeData);
		};
	}
}
