package software.bluelib.loader.json.deserialize.animation.keyframe;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;

import java.util.List;

public record KeyframeStackDeserializer<T extends KeyframeDeserializer<MoLangValue>>(
		@NotNull List<T> xKeyframes,
		@NotNull List<T> yKeyframes,
		@NotNull List<T> zKeyframes) {

	@NotNull
	public static JsonDeserializer<KeyframeStackDeserializer<KeyframeDeserializer<MoLangValue>>> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<KeyframeDeserializer<MoLangValue>> xKeyframes =
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "xKeyframes"), context,
							new TypeToken<KeyframeDeserializer<MoLangValue>>(){}.getType());
			List<KeyframeDeserializer<MoLangValue>> yKeyframes =
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "yKeyframes"), context,
							new TypeToken<KeyframeDeserializer<MoLangValue>>(){}.getType());
			List<KeyframeDeserializer<MoLangValue>> zKeyframes =
					JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "zKeyframes"), context,
							new TypeToken<KeyframeDeserializer<MoLangValue>>(){}.getType());

			return new KeyframeStackDeserializer<>(xKeyframes, yKeyframes, zKeyframes);
		};
	}
}