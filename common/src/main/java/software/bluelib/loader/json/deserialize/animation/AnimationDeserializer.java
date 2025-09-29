package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.loader.json.deserialize.animation.keyframe.CustomInstructionKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.ParticleKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.SoundKeyframeDeserializer;

import java.util.List;

public record AnimationDeserializer(
		@Nullable Double length,
		@Nullable String loopType,
		@NotNull List<BoneAnimationDeserializer> boneAnimation,
		@Nullable List<SoundKeyframeDeserializer> sounds,
		@Nullable List<ParticleKeyframeDeserializer> particles,
		@Nullable List<CustomInstructionKeyframeDeserializer> customInstructions) {

	@NotNull
	public static JsonDeserializer<AnimationDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Double length = JsonUtils.getOptionalDouble(obj, "animation_length");
			String loopType = parseLoopType(obj);
			List<BoneAnimationDeserializer> boneAnimations = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "bones", new JsonArray()), context, BoneAnimationDeserializer.class);
			List<SoundKeyframeDeserializer> sounds = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "sound_effects", new JsonArray()), context, SoundKeyframeDeserializer.class);
			List<ParticleKeyframeDeserializer> particles = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "particles", new JsonArray()), context, ParticleKeyframeDeserializer.class);
			List<CustomInstructionKeyframeDeserializer> customInstructions = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "custom?", new JsonArray()), context, CustomInstructionKeyframeDeserializer.class);

			// TODO: "particles", "custom?" are not the actual array names, verify the actual array names and parse them accordingly.

			return new AnimationDeserializer(
					length,
					loopType,
					boneAnimations,
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
