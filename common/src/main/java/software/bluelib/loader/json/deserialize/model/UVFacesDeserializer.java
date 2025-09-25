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
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;

public record UVFacesDeserializer(
		@Nullable FaceUVDeserializer north,
		@Nullable FaceUVDeserializer south,
		@Nullable FaceUVDeserializer east,
		@Nullable FaceUVDeserializer west,
		@Nullable FaceUVDeserializer up,
		@Nullable FaceUVDeserializer down) {

	@NotNull
	public static JsonDeserializer<UVFacesDeserializer> deserializer() {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			FaceUVDeserializer north = JsonUtils.getOptionalObject(obj, "north", context, FaceUVDeserializer.class);
			FaceUVDeserializer south = JsonUtils.getOptionalObject(obj, "south", context, FaceUVDeserializer.class);
			FaceUVDeserializer east = JsonUtils.getOptionalObject(obj, "east", context, FaceUVDeserializer.class);
			FaceUVDeserializer west = JsonUtils.getOptionalObject(obj, "west", context, FaceUVDeserializer.class);
			FaceUVDeserializer up = JsonUtils.getOptionalObject(obj, "up", context, FaceUVDeserializer.class);
			FaceUVDeserializer down = JsonUtils.getOptionalObject(obj, "down", context, FaceUVDeserializer.class);

			return new UVFacesDeserializer(
					north,
					south,
					east,
					west,
					up,
					down);
		};
	}

	public FaceUVDeserializer fromDirection(Direction pDirection) {
		return switch (pDirection) {
			case NORTH -> north;
			case SOUTH -> south;
			case EAST -> east;
			case WEST -> west;
			case UP -> up;
			case DOWN -> down;
		};
	}
}
