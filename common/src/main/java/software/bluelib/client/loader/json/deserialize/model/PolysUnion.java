/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

// TODO: Convert to Utils Please or Atleast Cleanup
public record PolysUnion(List<List<List<Float>>> union, @Nullable Type type) {

	public static JsonDeserializer<PolysUnion> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
				return new PolysUnion(new ArrayList<>(), context.deserialize(json.getAsJsonPrimitive(), Type.class));
			} else if (json.isJsonArray()) {
				JsonArray array = json.getAsJsonArray();
				List<List<List<Float>>> matrix = new ArrayList<>();

				for (int x = 0; x < array.size(); x++) {
					JsonArray xArray = array.get(x).getAsJsonArray();
					List<List<Float>> yList = new ArrayList<>();

					for (int y = 0; y < xArray.size(); y++) {
						JsonArray yArray = xArray.get(y).getAsJsonArray();
						List<Float> zList = JsonUtils.jsonArrayToFloatList(yArray);
						yList.add(zList);
					}

					matrix.add(yList);
				}

				return new PolysUnion(matrix, null);
			} else {
				throw new JsonParseException("Invalid format for PolysUnion, must be either string or array");
			}
		};
	}

	public enum Type {
		@SerializedName("quad_list")
		QUAD,

		@SerializedName("tri_list")
		TRI
	}
}
