/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record PolysUnion(
		@NotNull List<List<List<Float>>> union,
		@Nullable Type type) {

	@NotNull
	public static JsonDeserializer<PolysUnion> deserializer() throws JsonParseException {
		return (json, type, context) -> fromJson(json, context);
	}

	@NotNull
	public static PolysUnion fromJson(@NotNull JsonElement pJson, @NotNull JsonDeserializationContext pContext) throws JsonParseException {
		if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isString()) {
			Type type = pContext.deserialize(pJson.getAsJsonPrimitive(), PolysUnion.Type.class);
			return new PolysUnion(new ArrayList<>(), type);
		}
		if (pJson.isJsonArray()) {
			List<List<List<Float>>> matrix = new ArrayList<>();
			for (JsonElement xElem : pJson.getAsJsonArray()) {
				List<List<Float>> yList = new ArrayList<>();
				for (JsonElement yElem : xElem.getAsJsonArray()) {
					List<Float> zList = JsonUtils.jsonArrayToFloatList(yElem.getAsJsonArray());
					yList.add(zList);
				}
				matrix.add(yList);
			}
			return new PolysUnion(matrix, null);
		}
		throw new JsonParseException("Invalid format for PolysUnion, must be either string or array");
	}

	public enum Type {
		@SerializedName("quad_list")
		QUAD,

		@SerializedName("tri_list")
		TRI
	}
}
