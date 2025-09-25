/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;

public record UVUnionDeserializer(
		@NotNull List<Float> boxUVCoords,
		@Nullable UVFacesDeserializer faceUV,
		boolean isBoxUV) {

	@NotNull
	public static JsonDeserializer<UVUnionDeserializer> deserializer() {
		return JsonUtils.unionDeserializer(
				(arr, ctx) -> new UVUnionDeserializer(JsonUtils.jsonArrayToFloatList(arr), null, true),
				(obj, ctx) -> new UVUnionDeserializer(new ArrayList<>(), ctx.deserialize(obj, UVFacesDeserializer.class), false));
	}
}
