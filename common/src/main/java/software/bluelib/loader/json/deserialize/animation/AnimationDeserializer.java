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
		@NotNull String loopType,
		@NotNull BoneAnimationDeserializer[] boneAnimation,
		@NotNull SoundKeyframeDeserializer[] sounds,
		@NotNull ParticleKeyframeDeserializer[] particles,
		@NotNull CustomInstructionKeyframeDeserializer[] customInstructions) {

	@NotNull
	public static JsonDeserializer<AnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Double length = JsonUtils.getOptionalDouble(obj, "animation_length");
			String loopType = parseLoopType(obj.get("loop")); // TODO: Im here with Fixing Code, have already done AnimationFileDeserializer, Still need to do Cache
			BoneAnimationDeserializer[] boneAnimations = context.deserialize(
					GsonHelper.getAsJsonArray(obj, "bones", new JsonArray()),
					BoneAnimationDeserializer[].class);
			SoundKeyframeDeserializer[] sounds = context.deserialize(
					GsonHelper.getAsJsonArray(obj, "sounds", new JsonArray()),
					SoundKeyframeDeserializer[].class);
			ParticleKeyframeDeserializer[] particles = context.deserialize(
					GsonHelper.getAsJsonArray(obj, "particles", new JsonArray()),
					ParticleKeyframeDeserializer[].class);
			CustomInstructionKeyframeDeserializer[] customInstructions = context.deserialize(
					GsonHelper.getAsJsonArray(obj, "custom_instructions", new JsonArray()),
					CustomInstructionKeyframeDeserializer[].class);

			return new AnimationDeserializer(
					length,
					loopType,
					boneAnimations,
					sounds,
					particles,
					customInstructions);
		};
	}

	private static @NotNull String parseLoopType(@NotNull JsonElement pObj) {
		String value = "play_once";
		if (pObj.isJsonPrimitive()) {
			JsonPrimitive primitive = pObj.getAsJsonPrimitive();
			if (primitive.isBoolean()) {
				value = primitive.getAsBoolean() ? "loop" : "play_once";
			} else if (primitive.isString()) {
				value = primitive.getAsString();
			}
		}
		return value;
	}
}
