/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeData;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeDeserializer;

public record BoneAnimationDeserializer(
		@Nullable KeyframeData<MoLangValue> rotation,
		@Nullable KeyframeData<MoLangValue> position,
		@Nullable KeyframeData<MoLangValue> scale) {

	@NotNull
	public static JsonDeserializer<BoneAnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			KeyframeData<MoLangValue> rotationKeyFrames = parseData(obj, context, "rotation", MoLangValue::fromJson);
			KeyframeData<MoLangValue> positionKeyFrames = parseData(obj, context, "position", MoLangValue::fromJson);
			KeyframeData<MoLangValue> scaleKeyFrames = parseData(obj, context, "scale", MoLangValue::fromJson);

			return new BoneAnimationDeserializer(
					rotationKeyFrames,
					positionKeyFrames,
					scaleKeyFrames);
		};
	}

	@Nullable
	private static <T> KeyframeData<T> parseData(
			JsonObject pObj,
			JsonDeserializationContext pContext,
			String pMemberName,
			Function<JsonElement, T> pParser) {
		JsonElement element = pObj.get(pMemberName);
		if (element.isJsonArray()) {
			List<T> keyframes = JsonUtils.jsonArrayToList(element.getAsJsonArray(), pParser);
			return new KeyframeData.KeyframeArray<>(keyframes);
		} else if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			Map<String, KeyframeData<T>> keyframes = new HashMap<>();
			for (String keyframeName : obj.keySet()) {
				JsonElement keyframeData = obj.get(keyframeName);
				if (keyframeData.isJsonArray()) {
					keyframes.put(keyframeName, parseData(obj, pContext, keyframeName, pParser));
				} else if (keyframeData.isJsonObject()) {
					keyframes.put(keyframeName, parseData(obj, pContext, keyframeName, pElement -> pContext.deserialize(pElement, KeyframeDeserializer.class)));
				}
				return null;
			}
			return new KeyframeData.KeyframeObject<>(keyframes);
		}
		return null;
	}
}
