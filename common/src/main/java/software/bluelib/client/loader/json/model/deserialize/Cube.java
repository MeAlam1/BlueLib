package software.bluelib.client.loader.json.model.deserialize;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

import java.util.List;

public record Cube(
		List<Float> origin,
		List<Float> size,
		List<Float> pivot,
		List<Float> rotation,
		UVUnion uvUnion,
		@Nullable Float inflate,
		@Nullable Boolean mirror
) {
	public static JsonDeserializer<Cube> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			Float inflate = JsonUtils.getOptionalFloat(obj, "inflate");
			Boolean mirror = JsonUtils.getOptionalBoolean(obj, "mirror");

			List<Float> origin = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "origin", null));
			List<Float> size = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "size", null));
			List<Float> pivot = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "pivot", null));
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "rotation", null));
			UVUnion uvUnion = GsonHelper.getAsObject(obj, "uv", null, context, UVUnion.class);

			return new Cube(
					origin,
					size,
					pivot,
					rotation,
					uvUnion,
					inflate,
					mirror
			);
		};
	}
}
