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

public record LocatorValueDeserializer(
		@Nullable LocatorClassDeserializer locatorClassDeserializer,
		@NotNull List<Float> values) {

	@NotNull
	public static JsonDeserializer<LocatorValueDeserializer> deserializer() {
		return JsonUtils.unionDeserializer(
				(arr, ctx) -> new LocatorValueDeserializer(null, JsonUtils.jsonArrayToFloatList(arr)),
				(obj, ctx) -> new LocatorValueDeserializer(ctx.deserialize(obj, LocatorClassDeserializer.class), new ArrayList<>()));
	}
}
