/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeDeserializer;

public record BoneAnimationDeserializer(
		@Nullable List<MoLangValue> rotationArray,
		@Nullable KeyframeDeserializer rotationObject,
		@Nullable List<MoLangValue> positionArray,
		@Nullable KeyframeDeserializer positionObject,
		@Nullable List<MoLangValue> scaleArray,
		@Nullable KeyframeDeserializer scaleObject) {

	@NotNull
	public static JsonDeserializer<BoneAnimationDeserializer> deserializer() {
		return (json, typeOfT, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<MoLangValue> rotationArray = null;
			KeyframeDeserializer rotationObject = null;
			if (obj.has("rotation")) {
				JsonElement elem = obj.get("rotation");
				if (elem.isJsonArray()) {
					rotationArray = JsonUtils.jsonArrayToList(elem.getAsJsonArray(), MoLangValue::fromJson);
				} else if (elem.isJsonObject()) {
					rotationObject = context.deserialize(elem, KeyframeDeserializer.class);
				}
			}

			List<MoLangValue> positionArray = null;
			KeyframeDeserializer positionObject = null;
			if (obj.has("position")) {
				JsonElement elem = obj.get("position");
				if (elem.isJsonArray()) {
					positionArray = JsonUtils.jsonArrayToList(elem.getAsJsonArray(), MoLangValue::fromJson);
				} else if (elem.isJsonObject()) {
					positionObject = context.deserialize(elem, KeyframeDeserializer.class);
				}
			}

			List<MoLangValue> scaleArray = null;
			KeyframeDeserializer scaleObject = null;
			if (obj.has("scale")) {
				JsonElement elem = obj.get("scale");
				if (elem.isJsonArray()) {
					scaleArray = JsonUtils.jsonArrayToList(elem.getAsJsonArray(), MoLangValue::fromJson);
				} else if (elem.isJsonObject()) {
					scaleObject = context.deserialize(elem, KeyframeDeserializer.class);
				}
			}

			return new BoneAnimationDeserializer(
					rotationArray, rotationObject,
					positionArray, positionObject,
					scaleArray, scaleObject);
		};
	}
}
