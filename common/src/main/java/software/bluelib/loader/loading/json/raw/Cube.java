package software.bluelib.loader.loading.json.raw;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.util.JsonUtil;


public record Cube(@Nullable Double inflate, @Nullable Boolean mirror, double[] origin, double[] pivot, double[] rotation, double[] size, UVUnion uv) {
	public static JsonDeserializer<Cube> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			Double inflate = JsonUtil.getOptionalDouble(obj, "inflate");
			Boolean mirror = JsonUtil.getOptionalBoolean(obj, "mirror");
			double[] origin = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "origin", null));
			double[] pivot = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "pivot", null));
			double[] rotation = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "rotation", null));
			double[] size = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "size", null));
			UVUnion uvUnion = GsonHelper.getAsObject(obj, "uv", null, context, UVUnion.class);

			return new Cube(inflate, mirror, origin, pivot, rotation, size, uvUnion);
		};
	}
}
