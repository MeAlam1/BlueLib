/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;

public record LocatorClassDeserializer(
		@Nullable Boolean ignoreInheritedScale,
		@NotNull List<Float> offset,
		@NotNull List<Float> rotation) {

	@NotNull
	public static JsonDeserializer<LocatorClassDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			Boolean ignoreInheritedScale = JsonUtils.getOptionalBoolean(obj, "ignore_inherited_scale");
			List<Float> offset = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "offset"));
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "rotation"));

			return new LocatorClassDeserializer(
					ignoreInheritedScale,
					offset,
					rotation);
		};
	}
}
