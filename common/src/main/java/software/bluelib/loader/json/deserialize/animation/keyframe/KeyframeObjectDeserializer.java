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

public record KeyframeObjectDeserializer(
		@Nullable MoLangValue pre,
		@Nullable MoLangValue post,
		@Nullable String easing,
		@Nullable List<MoLangValue> easingArgs) implements KeyframeDeserializerData {

	@NotNull
	public static JsonDeserializer<KeyframeObjectDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			MoLangValue pre = MoLangValue.fromJson(GsonHelper.getAsJsonObject(obj, "pre"));
			MoLangValue post = MoLangValue.fromJson(GsonHelper.getAsJsonObject(obj, "post"));
			String lerp_mode = GsonHelper.getAsString(obj, "lerp_mode", "linear");
			List<MoLangValue> args = JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(obj, "easingArgs"), MoLangValue::fromJson);

			return new KeyframeObjectDeserializer(
					pre,
					post,
					lerp_mode,
					args);
		};
	}
}
