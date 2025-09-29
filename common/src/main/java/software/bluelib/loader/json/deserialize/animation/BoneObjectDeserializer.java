/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeDeserializer;

public record BoneObjectDeserializer(
		@Nullable KeyframeDeserializer rotation,
		@Nullable KeyframeDeserializer position,
		@Nullable KeyframeDeserializer scale) implements BoneAnimationDeserializer {

	@NotNull
	public static JsonDeserializer<BoneObjectDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			KeyframeDeserializer rotationKeyframes = JsonUtils.getOptionalObject(obj, "rotation", context, KeyframeDeserializer.class);
			KeyframeDeserializer positionKeyframes = JsonUtils.getOptionalObject(obj, "position", context, KeyframeDeserializer.class);
			KeyframeDeserializer scaleKeyframes = JsonUtils.getOptionalObject(obj, "scale", context, KeyframeDeserializer.class);

			return new BoneObjectDeserializer(
					rotationKeyframes,
					positionKeyframes,
					scaleKeyframes);
		};
	}
}
