/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import java.util.Map;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;

public record BoneDeserializer(
		@NotNull List<Float> bindPoseRotation,
		@NotNull List<CubeDeserializer> cubeDeserializers,
		@Nullable Boolean debug,
		@Nullable Float inflate,
		@Nullable Map<String, LocatorValueDeserializer> locators,
		@Nullable Boolean mirror,
		@Nullable String name,
		@Nullable Boolean neverRender,
		@Nullable String parent,
		@NotNull List<Float> pivot,
		@Nullable PolyMeshDeserializer polyMeshDeserializer,
		@Nullable Long renderGroupId,
		@Nullable Boolean reset,
		@NotNull List<Float> rotation,
		@Nullable List<TextureMeshDeserializer> textureMeshDeserializers) {

	@NotNull
	public static JsonDeserializer<BoneDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			List<Float> bindPoseRotation = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "bind_pose_rotation"));
			List<CubeDeserializer> cubeDeserializers = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "cubes", new JsonArray(0)), context, CubeDeserializer.class);
			Boolean debug = JsonUtils.getOptionalBoolean(obj, "debug");
			Float inflate = JsonUtils.getOptionalFloat(obj, "inflate");
			Map<String, LocatorValueDeserializer> locators = obj.has("locators")
					? JsonUtils.jsonObjToMap(GsonHelper.getAsJsonObject(obj, "locators"), context, LocatorValueDeserializer.class)
					: null;
			Boolean mirror = JsonUtils.getOptionalBoolean(obj, "mirror");
			String name = JsonUtils.getOptionalString(obj, "name");
			Boolean neverRender = JsonUtils.getOptionalBoolean(obj, "neverRender");
			String parent = JsonUtils.getOptionalString(obj, "parent");
			List<Float> pivot = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "pivot", new JsonArray(0)));
			PolyMeshDeserializer polyMeshDeserializer = JsonUtils.getOptionalObject(obj, "poly_mesh", context, PolyMeshDeserializer.class);
			Long renderGroupId = JsonUtils.getOptionalLong(obj, "render_group_id");
			Boolean reset = JsonUtils.getOptionalBoolean(obj, "reset");
			List<Float> rotation = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "rotation"));
			List<TextureMeshDeserializer> textureMeshDeserializers = JsonUtils.jsonArrayToObjectList(GsonHelper.getAsJsonArray(obj, "texture_meshes", new JsonArray(0)), context, TextureMeshDeserializer.class);

			return new BoneDeserializer(
					bindPoseRotation,
					cubeDeserializers,
					debug,
					inflate,
					locators,
					mirror,
					name,
					neverRender,
					parent,
					pivot,
					polyMeshDeserializer,
					renderGroupId,
					reset,
					rotation,
					textureMeshDeserializers);
		};
	}
}
