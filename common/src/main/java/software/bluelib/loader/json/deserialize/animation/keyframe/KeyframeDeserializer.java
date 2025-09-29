/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation.keyframe;

import com.google.gson.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;

public record KeyframeDeserializer(
		@NotNull Map<String, KeyframeDeserializerData> keyframeData) {

	@NotNull
	public static JsonDeserializer<KeyframeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Map<String, KeyframeDeserializerData> keyframeData = new HashMap<>();
			for (String keyframeName : obj.keySet()) {
				JsonElement keyframeElement = obj.get(keyframeName);
				keyframeData.put(keyframeName, parseData(keyframeElement, context));
			}

			return new KeyframeDeserializer(
					keyframeData);
		};
	}

	@Nullable
	private static KeyframeDeserializerData parseData(
			JsonElement pElement,
			JsonDeserializationContext pContext) {
		if (pElement.isJsonArray()) {
			List<MoLangValue> list = new ArrayList<>();
			for (JsonElement element : pElement.getAsJsonArray()) {
				list.add(MoLangValue.fromJson(element));
			}
			return new KeyframeArrayDeserializer(list);
		} else if (pElement.isJsonObject()) {
			return pContext.deserialize(pElement, KeyframeObjectDeserializer.class);
		}
		return null;
	}

	;
}
