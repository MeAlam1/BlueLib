package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;


public record LoopTypeDeserializer(
		@NotNull String name
) {

	@NotNull
	public static JsonDeserializer<LoopTypeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			String value = "play_once";
			if (json != null && json.isJsonPrimitive()) {
				JsonPrimitive primitive = json.getAsJsonPrimitive();
				if (primitive.isBoolean()) {
					value = primitive.getAsBoolean() ? "loop" : "play_once";
				} else if (primitive.isString()) {
					value = primitive.getAsString();
				}
			}
			return new LoopTypeDeserializer(value);
		};
	}
}
