/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.api.utils.loader.JsonUtils;

public record BoneArrayDeserializer(
		@Nullable List<MoLangValue> rotation,
		@Nullable List<MoLangValue> position,
		@Nullable List<MoLangValue> scale) implements BoneAnimationDeserializer {

	@NotNull
	public static JsonDeserializer<BoneArrayDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<MoLangValue> rotationKeyframes = JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(obj, "rotation"), MoLangValue::fromJson);
			List<MoLangValue> positionKeyframes = JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(obj, "position"), MoLangValue::fromJson);
			List<MoLangValue> scaleKeyframes = JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(obj, "scale"), MoLangValue::fromJson);

			return new BoneArrayDeserializer(
					rotationKeyframes,
					positionKeyframes,
					scaleKeyframes);
		};
	}
}
