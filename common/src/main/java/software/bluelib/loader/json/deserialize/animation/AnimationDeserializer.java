package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;

public record AnimationDeserializer(
		@NotNull String name,
		double length,
		@NotNull String loopType,
		@NotNull BoneAnimationDeserializer[] boneAnimation,
		@NotNull SoundKeyframeDeserializer[] sounds,
		@NotNull ParticleKeyframeDeserializer[] particles,
		@NotNull CustomInstructionKeyframeDeserializer[] customInstructions) {

	@NotNull
	public static JsonDeserializer<AnimationsDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String loopType = obj.get("loop").getAsString();

			return new AnimationDeserializer();
		};
	}
}
