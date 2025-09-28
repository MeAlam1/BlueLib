package software.bluelib.loader.json.deserialize.animation.keyframe;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public record ParticleKeyframeDeserializer(
		double startTick,
		@NotNull String effect,
		@NotNull String locator,
		@NotNull String script
) {

	@NotNull
	public static JsonDeserializer<ParticleKeyframeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			double startTick = GsonHelper.getAsDouble(obj, "startTick");
			String effect = GsonHelper.getAsString(obj, "effect");
			String locator = GsonHelper.getAsString(obj, "locator");
			String script = GsonHelper.getAsString(obj, "script");

			return new ParticleKeyframeDeserializer(
					startTick,
					effect,
					locator,
					script
			);
		};
	}
}
