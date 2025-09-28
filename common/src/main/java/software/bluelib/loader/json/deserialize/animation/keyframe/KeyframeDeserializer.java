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

public record KeyframeDeserializer<T extends MoLangValue>(
		double length,
		@Nullable T startValue,
		@Nullable T endValue,
		@Nullable String easing,
		@Nullable List<T> easingArgs) {

	@NotNull
	public static JsonDeserializer<KeyframeDeserializer<?>> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			double length = GsonHelper.getAsDouble(obj, "length");
			MoLangValue start = MoLangValue.fromJson(GsonHelper.getAsJsonObject(obj, "pre"));
			MoLangValue end = MoLangValue.fromJson(GsonHelper.getAsJsonObject(obj, "post"));
			String lerp_mode = GsonHelper.getAsString(obj, "lerp_mode", "linear");
			List<MoLangValue> args = JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(obj, "easingArgs"), MoLangValue::fromJson);

			return new KeyframeDeserializer<>(
					length,
					start,
					end,
					lerp_mode,
					null
			);
		};
	}
}
