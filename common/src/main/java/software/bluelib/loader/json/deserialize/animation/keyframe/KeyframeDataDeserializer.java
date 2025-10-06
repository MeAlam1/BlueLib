/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation.keyframe;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;

public record KeyframeDataDeserializer(
		@Nullable List<MoLangValue> arrayData,
		@Nullable List<MoLangValue> pre,
		@Nullable List<MoLangValue> post,
		@Nullable String easing,
		@Nullable List<MoLangValue> easingArgs) {

	@NotNull
	public static JsonDeserializer<KeyframeDataDeserializer> deserializer() {
		return (json, typeOfT, context) -> {
			System.out.println("[KeyframeDataDeserializer] JSON type: " + (json.isJsonArray() ? "Array" : json.isJsonObject() ? "Object" : "Other") + " | Content: " + json);

			if (json.isJsonArray()) {
				List<MoLangValue> array = JsonUtils.jsonArrayToList(json.getAsJsonArray(), MoLangValue::fromJson);
				return new KeyframeDataDeserializer(array, null, null, null, null);
			} else if (json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();

				List<MoLangValue> pre = obj.has("pre") ? JsonUtils.jsonArrayToList(obj.getAsJsonArray("pre"), MoLangValue::fromJson) : null;
				List<MoLangValue> post = obj.has("post") ? JsonUtils.jsonArrayToList(obj.getAsJsonArray("post"), MoLangValue::fromJson) : null;
				String easing = GsonHelper.getAsString(obj, "lerp_mode", "linear");
				List<MoLangValue> easingArgs = obj.has("easingArgs")
						? JsonUtils.jsonArrayToList(obj.getAsJsonArray("easingArgs"), MoLangValue::fromJson)
						: null;

				return new KeyframeDataDeserializer(null, pre, post, easing, easingArgs);
			} else {
				throw new JsonParseException("KeyframeDeserializerData must be an array or object");
			}
		};
	}
}
