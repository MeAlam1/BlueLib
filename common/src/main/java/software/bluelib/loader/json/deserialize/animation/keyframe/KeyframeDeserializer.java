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
import software.bluelib.api.math.Vector3s;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.animation.math.Easing;

import java.util.List;

public record KeyframeDeserializer<T extends MoLangValue>(
		double length,
		@NotNull T startValue,
		@NotNull T endValue,
		@NotNull Easing easing, // TODO: Make custom Deserializer + Cache for Easing
		@Nullable List<T> easingArgs) {

	@NotNull
	public static JsonDeserializer<KeyframeDeserializer<?>> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			
			double length = GsonHelper.getAsDouble(obj, "length");

			return new KeyframeDeserializer<>(
			);
		};
	}
}
