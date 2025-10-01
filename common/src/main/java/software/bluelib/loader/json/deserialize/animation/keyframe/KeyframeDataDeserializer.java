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
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;

import java.util.List;

public record KeyframeDataDeserializer(
		@Nullable List<MoLangValue> arrayData,
		@Nullable MoLangValue pre,
		@Nullable MoLangValue post,
		@Nullable String easing,
		@Nullable List<MoLangValue> easingArgs
) {
	@NotNull
	public static JsonDeserializer<KeyframeDataDeserializer> deserializer() {
		return (json, typeOfT, context) -> {
			if (json.isJsonArray()) {
				List<MoLangValue> array = JsonUtils.jsonArrayToList(json.getAsJsonArray(), MoLangValue::fromJson);
				return new KeyframeDataDeserializer(array, null, null, null, null);
			} else if (json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();

				MoLangValue pre = obj.has("pre") ? MoLangValue.fromJson(obj.getAsJsonObject("pre")) : null;
				MoLangValue post = obj.has("post") ? MoLangValue.fromJson(obj.getAsJsonObject("post")) : null;
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
