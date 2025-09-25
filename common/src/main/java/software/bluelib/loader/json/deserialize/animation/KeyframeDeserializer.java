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
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bluelib.api.json.deserializer.pos.Vector3;
import software.bluelib.api.math.Vector3s;
import software.bluelib.loader.animation.math.Easing;
import software.bluelib.loader.geckolib.math.MathValue;

import java.util.List;

public record KeyframeDeserializer<T extends Vector3s>(
		double length,
		@NotNull T startValue,
		@NotNull T endValue,
		@NotNull Easing easing,
		@NotNull List<T> easingArgs) {

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
