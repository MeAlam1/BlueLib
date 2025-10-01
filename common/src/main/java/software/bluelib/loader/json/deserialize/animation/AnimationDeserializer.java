/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.loader.json.deserialize.animation.keyframe.CustomInstructionKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.ParticleKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.SoundKeyframeDeserializer;

public record AnimationDeserializer(
		@Nullable Double length,
		@Nullable String loopType,
		@NotNull BoneAnimationsDeserializer bones,
		@Nullable SoundKeyframeDeserializer sounds,
		@Nullable ParticleKeyframeDeserializer particles,
		@Nullable CustomInstructionKeyframeDeserializer customInstructions) {

	@NotNull
	public static JsonDeserializer<AnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Double length = JsonUtils.getOptionalDouble(obj, "animation_length");
			String loopType = parseLoopType(obj);
			BoneAnimationsDeserializer bones = GsonHelper.getAsObject(obj, "bones", context, BoneAnimationsDeserializer.class);
			SoundKeyframeDeserializer sounds = JsonUtils.getOptionalObject(obj, "sound_effects", context, SoundKeyframeDeserializer.class);
			ParticleKeyframeDeserializer particles = JsonUtils.getOptionalObject(obj, "particles", context, ParticleKeyframeDeserializer.class);
			CustomInstructionKeyframeDeserializer customInstructions = JsonUtils.getOptionalObject(obj, "custom?", context, CustomInstructionKeyframeDeserializer.class);

			// TODO: "particles", "custom?" are not the actual array names, verify the actual array names and parse them accordingly.

			return new AnimationDeserializer(
					length,
					loopType,
					bones,
					sounds,
					particles,
					customInstructions);
		};
	}

	private static @NotNull String parseLoopType(@NotNull JsonObject pObj) {
		JsonPrimitive primitive = JsonUtils.getOptionalPrimitive(pObj, "loop");
		if (primitive != null) {
			if (primitive.isBoolean()) {
				return primitive.getAsBoolean() ? "loop" : "play_once";
			} else if (primitive.isString()) {
				return primitive.getAsString();
			}
		}
		return "play_once";
	}
}
