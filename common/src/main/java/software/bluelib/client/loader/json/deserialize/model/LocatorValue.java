/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

// TODO: Convert to Utils Please or Atleast Cleanup
public record LocatorValue(@Nullable LocatorClass locatorClass, List<Float> values) {

	public static JsonDeserializer<LocatorValue> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			if (json.isJsonArray()) {
				return new LocatorValue(null, JsonUtils.jsonArrayToFloatList(json.getAsJsonArray()));
			} else if (json.isJsonObject()) {
				return new LocatorValue(context.deserialize(json.getAsJsonObject(), LocatorClass.class), new ArrayList<>());
			} else {
				throw new JsonParseException("Invalid format for LocatorValue in json");
			}
		};
	}
}
