package software.bluelib.loader.loading.json.raw;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.util.JsonUtil;


public record LocatorValue(@Nullable LocatorClass locatorClass, double[] values) {
	public static JsonDeserializer<LocatorValue> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			if (json.isJsonArray()) {
				return new LocatorValue(null, JsonUtil.jsonArrayToDoubleArray(json.getAsJsonArray()));
			}
			else if (json.isJsonObject()) {
				return new LocatorValue(context.deserialize(json.getAsJsonObject(), LocatorClass.class), new double[0]);
			}
			else {
				throw new JsonParseException("Invalid format for LocatorValue in json");
			}
		};
	}
}
