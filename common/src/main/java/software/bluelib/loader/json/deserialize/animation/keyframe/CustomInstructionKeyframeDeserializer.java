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

public record CustomInstructionKeyframeDeserializer(
		double startTick,
		@NotNull String instructions) {

	@NotNull
	public static JsonDeserializer<CustomInstructionKeyframeDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			double startTick = GsonHelper.getAsDouble(obj, "startTick");
			String instructions = GsonHelper.getAsString(obj, "instructions");

			return new CustomInstructionKeyframeDeserializer(
					startTick,
					instructions);
		};
	}
}
