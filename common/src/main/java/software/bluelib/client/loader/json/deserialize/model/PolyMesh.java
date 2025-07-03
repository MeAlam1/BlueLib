/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record PolyMesh(
		@Nullable Boolean normalizedUVs,
		List<Float> normals,
		@Nullable PolysUnion polysUnion,
		List<Float> positions,
		List<Float> uvs) {

	public static JsonDeserializer<PolyMesh> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			Boolean normalizedUVs = JsonUtils.getOptionalBoolean(obj, "normalized_uvs");
			List<Float> normals = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "normals"));
			PolysUnion polysUnion = JsonUtils.getOptionalObject(obj, "polys", context, PolysUnion.class);
			List<Float> positions = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "positions"));
			List<Float> uvs = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "uvs"));

			return new PolyMesh(
					normalizedUVs,
					normals,
					polysUnion,
					positions,
					uvs);
		};
	}
}
