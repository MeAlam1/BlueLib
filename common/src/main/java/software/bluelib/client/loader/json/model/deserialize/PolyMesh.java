/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.deserialize;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

import java.util.List;

public record PolyMesh(
		@Nullable Boolean normalizedUVs,
		List<Float> normals,
		@Nullable PolysUnion polysUnion,
		List<Float> positions,
		List<Float> uvs
) {
	public static JsonDeserializer<PolyMesh> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			Boolean normalizedUVs = JsonUtils.getOptionalBoolean(obj, "normalized_uvs");
			List<Float> normals = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "normals", null));
			PolysUnion polysUnion = GsonHelper.getAsObject(obj, "polys", null, context, PolysUnion.class);
			List<Float> positions = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "positions", null));
			List<Float> uvs = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "uvs", null));

			return new PolyMesh(
					normalizedUVs,
					normals,
					polysUnion,
					positions,
					uvs
			);
		};
	}
}
