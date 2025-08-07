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

public record LocatorValue(
		@Nullable LocatorClass locatorClass,
		@NotNull List<Float> values) {

	@NotNull
	public static JsonDeserializer<LocatorValue> deserializer() {
		return JsonUtils.unionDeserializer(
				(arr, ctx) -> new LocatorValue(null, JsonUtils.jsonArrayToFloatList(arr)),
				(obj, ctx) -> new LocatorValue(ctx.deserialize(obj, LocatorClass.class), new ArrayList<>()));
	}
}
