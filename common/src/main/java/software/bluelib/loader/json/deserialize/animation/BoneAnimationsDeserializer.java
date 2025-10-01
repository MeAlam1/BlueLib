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
import com.google.gson.JsonParseException;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public record BoneAnimationsDeserializer(
		@NotNull Map<String, BoneAnimationDeserializer> boneAnimations

) {

	@NotNull
	public static JsonDeserializer<BoneAnimationsDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Map<String, BoneAnimationDeserializer> boneAnimations = new HashMap<>();
			for (String boneName : obj.keySet()) {
				JsonElement boneElement = obj.get(boneName);
				boneAnimations.put(boneName, context.deserialize(boneElement, BoneAnimationDeserializer.class));

			}

			return new BoneAnimationsDeserializer(
					boneAnimations);
		};
	}
}
