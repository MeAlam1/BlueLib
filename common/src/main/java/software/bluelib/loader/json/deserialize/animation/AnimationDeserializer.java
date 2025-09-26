package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.json.deserialize.animation.keyframe.CustomInstructionKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.ParticleKeyframeDeserializer;
import software.bluelib.loader.json.deserialize.animation.keyframe.SoundKeyframeDeserializer;

public record AnimationDeserializer(
		@NotNull String name,
		double length,
		@NotNull LoopTypeDeserializer loopType,
		@NotNull BoneAnimationDeserializer[] boneAnimation,
		@NotNull SoundKeyframeDeserializer[] sounds,
		@NotNull ParticleKeyframeDeserializer[] particles,
		@NotNull CustomInstructionKeyframeDeserializer[] customInstructions) {

	@NotNull
	public static JsonDeserializer<AnimationsDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			return new AnimationDeserializer();
		};
	}
}
