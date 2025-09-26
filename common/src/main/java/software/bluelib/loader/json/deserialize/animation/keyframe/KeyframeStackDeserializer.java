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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record KeyframeStackDeserializer<T extends KeyframeDeserializer<?>>(
		@NotNull List<T> xKeyframes,
		@NotNull List<T> yKeyframes,
		@NotNull List<T> zKeyframes) {

	@NotNull
	public static JsonDeserializer<KeyframeStackDeserializer<?>> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			return new KeyframeStackDeserializer<>(
			);
		};
	}
}
