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
import org.joml.Vector3f;

public record BoneAnimationDeserializer(
		@NotNull String boneName,
		@NotNull KeyframeStackDeserializer<KeyframeDeserializer<Vector3f>> rotationKeyFrames,
		@NotNull KeyframeStackDeserializer<KeyframeDeserializer<Vector3f>> positionKeyFrames,
		@NotNull KeyframeStackDeserializer<KeyframeDeserializer<Vector3f>> scaleKeyFrames) {

	@NotNull
	public static JsonDeserializer<BoneAnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String boneName = GsonHelper.getAsString(obj, "boneName");

			return new BoneAnimationDeserializer(
					boneName
			);
		};
	}
}
