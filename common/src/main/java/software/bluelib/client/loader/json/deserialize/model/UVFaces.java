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
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record UVFaces(
		@Nullable FaceUV north,
		@Nullable FaceUV south,
		@Nullable FaceUV east,
		@Nullable FaceUV west,
		@Nullable FaceUV up,
		@Nullable FaceUV down) {

	public static JsonDeserializer<UVFaces> deserializer() {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();
			FaceUV north = JsonUtils.getOptionalObject(obj, "north", context, FaceUV.class);
			FaceUV south = JsonUtils.getOptionalObject(obj, "south", context, FaceUV.class);
			FaceUV east = JsonUtils.getOptionalObject(obj, "east", context, FaceUV.class);
			FaceUV west = JsonUtils.getOptionalObject(obj, "west", context, FaceUV.class);
			FaceUV up = JsonUtils.getOptionalObject(obj, "up", context, FaceUV.class);
			FaceUV down = JsonUtils.getOptionalObject(obj, "down", context, FaceUV.class);

			return new UVFaces(
					north,
					south,
					east,
					west,
					up,
					down);
		};
	}

	public FaceUV fromDirection(Direction pDirection) {
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
