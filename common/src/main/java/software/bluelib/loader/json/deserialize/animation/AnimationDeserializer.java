package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.json.deserialize.animation.keyframe.CustomInstructionKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.ParticleKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.SoundKeyframeDeserializer;

public record AnimationDeserializer(
		@NotNull String name,
		double length,
		@NotNull String loopType,
		@NotNull BoneAnimationDeserializer[] boneAnimation,
		@NotNull SoundKeyframeDeserializer[] sounds,
		@NotNull ParticleKeyframeDeserializer[] particles,
		@NotNull CustomInstructionKeyframeDeserializer[] customInstructions) {

	@NotNull
	public static JsonDeserializer<AnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String name = GsonHelper.getAsString(obj, "name", "unknown");
			double length = GsonHelper.getAsDouble(obj, "length", 1.0);
			String loopType = parseLoopType(obj.get("loop_type"));
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
					name,
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
