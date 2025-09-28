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
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeStackDeserializer;

public record BoneAnimationDeserializer(
		@NotNull String boneName,
		@NotNull KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>> rotationKeyFrames,
		@NotNull KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>> positionKeyFrames,
		@NotNull KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>> scaleKeyFrames) {

	@NotNull
	public static JsonDeserializer<BoneAnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String boneName = GsonHelper.getAsString(obj, "boneName");
			KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>> rotationKeyFrames = GsonHelper.getAsObject(obj, "rotation", context, KeyframeStackDeserializer.class);
			KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>> positionKeyFrames = GsonHelper.getAsObject(obj, "position", context, KeyframeStackDeserializer.class);
			KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>> scaleKeyFrames = GsonHelper.getAsObject(obj, "scale", context, KeyframeStackDeserializer.class);

			return new BoneAnimationDeserializer(
					boneName,
					rotationKeyFrames,
					positionKeyFrames,
					scaleKeyFrames
			);
		};
	}
}
