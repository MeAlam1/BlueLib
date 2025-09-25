/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.CubeCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.model.*;
import software.bluelib.loader.json.model.object.BoneStructure;
import software.bluelib.loader.json.model.object.BoneTree;
import software.bluelib.loader.json.object.QuadData;
import software.bluelib.loader.json.object.VertexData;

public interface ModelCacheFactory extends CacheFactory<ModelCache, ModelDeserializer> {

	@NotNull
	Map<String, ModelCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	@NotNull
	ModelCacheFactory DEFAULT_FACTORY = new Builtin();

	@NotNull
	CacheFactory.Registry<ModelCache, ModelDeserializer, ModelCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

		@Override
		public @NotNull Map<String, ModelCacheFactory> factories() {
			return FACTORIES;
		}

		@Override
		public @NotNull ModelCacheFactory defaultFactory() {
			return DEFAULT_FACTORY;
		}
	};

	@Override
	default @NotNull ModelCache construct(@NotNull ModelDeserializer pSource) {
		return constructBlueModel(pSource);
	}

	@NotNull
	ModelCache constructBlueModel(@NotNull ModelDeserializer pModelDeserializer);

	@NotNull
	BoneCache constructBone(@NotNull BoneStructure pBoneStructure, @Nullable ModelDescriptionDeserializer pModelDescriptionDeserializer, @Nullable BoneCache pParent);

	@NotNull
	CubeCache constructCube(@NotNull CubeDeserializer pCubeDeserializer, @Nullable ModelDescriptionDeserializer pModelDescriptionDeserializer, @NotNull BoneCache pBone);

	default @NotNull List<QuadData> buildQuads(@NotNull UVUnionDeserializer pUvUnionDeserializer, @NotNull VertexSet pVertices, @NotNull CubeDeserializer pCubeDeserializer, @NotNull Float pTextureWidth, @NotNull Float pTextureHeight, boolean pMirror) {
		List<QuadData> quads = new ArrayList<>(6);

		quads.add(buildQuad(pVertices, pCubeDeserializer, pUvUnionDeserializer, pTextureWidth, pTextureHeight, pMirror, Direction.WEST));
		quads.add(buildQuad(pVertices, pCubeDeserializer, pUvUnionDeserializer, pTextureWidth, pTextureHeight, pMirror, Direction.EAST));
		quads.add(buildQuad(pVertices, pCubeDeserializer, pUvUnionDeserializer, pTextureWidth, pTextureHeight, pMirror, Direction.NORTH));
		quads.add(buildQuad(pVertices, pCubeDeserializer, pUvUnionDeserializer, pTextureWidth, pTextureHeight, pMirror, Direction.SOUTH));
		quads.add(buildQuad(pVertices, pCubeDeserializer, pUvUnionDeserializer, pTextureWidth, pTextureHeight, pMirror, Direction.UP));
		quads.add(buildQuad(pVertices, pCubeDeserializer, pUvUnionDeserializer, pTextureWidth, pTextureHeight, pMirror, Direction.DOWN));

		return quads;
	}

	default @NotNull QuadData buildQuad(@NotNull VertexSet pVertices, @NotNull CubeDeserializer pCubeDeserializer, @NotNull UVUnionDeserializer pUvUnionDeserializer, @NotNull Float pTextureWidth, @NotNull Float pTextureHeight, boolean pMirror, @NotNull Direction pDirection) {
		if (!pUvUnionDeserializer.isBoxUV()) {
			FaceUVDeserializer faceUVDeserializer = pUvUnionDeserializer.faceUV().fromDirection(pDirection);

			return QuadData.build(pVertices.verticesForQuad(pDirection, false, pMirror || pCubeDeserializer.mirror() == Boolean.TRUE), faceUVDeserializer.uv(), faceUVDeserializer.uvSize(),
					faceUVDeserializer.uvRotation(), pTextureWidth, pTextureHeight, pMirror, pDirection);
		}

		List<Float> uv = pCubeDeserializer.uvUnionDeserializer().boxUVCoords();
		List<Float> uvSize = pCubeDeserializer.size();
		Vec3 uvSizeVec = new Vec3(
				(float) Math.floor(uvSize.get(0)),
				(float) Math.floor(uvSize.get(1)),
				(float) Math.floor(uvSize.get(2)));

		List<List<Float>> uvData = switch (pDirection) {
			case WEST -> List.of(
					List.of(
							uv.get(0) + (float) uvSizeVec.z + (float) uvSizeVec.x,
							uv.get(1) + (float) uvSizeVec.z),
					List.of(
							(float) uvSizeVec.z,
							(float) uvSizeVec.y));
			case EAST -> List.of(
					List.of(
							uv.get(0),
							uv.get(1) + (float) uvSizeVec.z),
					List.of(
							(float) uvSizeVec.z,
							(float) uvSizeVec.y));
			case NORTH -> List.of(
					List.of(
							uv.get(0) + (float) uvSizeVec.z,
							uv.get(1) + (float) uvSizeVec.z),
					List.of(
							(float) uvSizeVec.x,
							(float) uvSizeVec.y));
			case SOUTH -> List.of(
					List.of(
							uv.get(0) + (float) uvSizeVec.z + (float) uvSizeVec.x + (float) uvSizeVec.z,
							uv.get(1) + (float) uvSizeVec.z),
					List.of(
							(float) uvSizeVec.x,
							(float) uvSizeVec.y));
			case UP -> List.of(
					List.of(
							uv.get(0) + (float) uvSizeVec.z,
							uv.get(1)),
					List.of(
							(float) uvSizeVec.x,
							(float) uvSizeVec.z));
			case DOWN -> List.of(
					List.of(
							uv.get(0) + (float) uvSizeVec.z + (float) uvSizeVec.x,
							uv.get(1) + (float) uvSizeVec.z),
					List.of(
							(float) uvSizeVec.x,
							-(float) uvSizeVec.z));
		};

		return QuadData.build(pVertices.verticesForQuad(pDirection, true, pMirror || pCubeDeserializer.mirror() == Boolean.TRUE), uvData.get(0), uvData.get(1), FaceUVDeserializer.Rotation.NONE, pTextureWidth, pTextureHeight, pMirror, pDirection);
	}

	final class Builtin implements ModelCacheFactory {

		@Override
		public @NotNull ModelCache constructBlueModel(@NotNull ModelDeserializer pModelDeserializer) {
			BoneTree boneTree = BoneTree.fromModel(pModelDeserializer);

			List<BoneCache> bones = new ObjectArrayList<>();

			for (BoneStructure boneStructure : boneTree.topLevelBones().values()) {
				bones.add(constructBone(boneStructure, boneTree.description(), null));
			}

			return new ModelCache(bones, boneTree.description());
		}

		@Override
		public @NotNull BoneCache constructBone(@NotNull BoneStructure pBoneStructure, @Nullable ModelDescriptionDeserializer pModelDescriptionDeserializer, @Nullable BoneCache pParent) {
			BoneDeserializer boneDeserializer = pBoneStructure.self();
			BoneCache newBone = new BoneCache(pParent, boneDeserializer.name(), boneDeserializer.mirror(), boneDeserializer.inflate(), boneDeserializer.neverRender(), boneDeserializer.reset());
			Vec3 rotation = RenderUtils.listToVec(boneDeserializer.rotation());
			Vec3 pivot = RenderUtils.listToVec(boneDeserializer.pivot());

			newBone.updateRotation((float) Math.toRadians(-rotation.x), (float) Math.toRadians(-rotation.y), (float) Math.toRadians(rotation.z));
			newBone.updatePivot((float) -pivot.x, (float) pivot.y, (float) pivot.z);

			for (CubeDeserializer cubeDeserializer : boneDeserializer.cubeDeserializers()) {
				newBone.getCubes().add(constructCube(cubeDeserializer, pModelDescriptionDeserializer, newBone));
			}

			for (BoneStructure child : pBoneStructure.children().values()) {
				newBone.getChildBones().add(constructBone(child, pModelDescriptionDeserializer, newBone));
			}

			return newBone;
		}

		@Override
		public @NotNull CubeCache constructCube(@NotNull CubeDeserializer pCubeDeserializer, @Nullable ModelDescriptionDeserializer pModelDescriptionDeserializer, @NotNull BoneCache pBone) {
			boolean mirror = pCubeDeserializer.mirror() == Boolean.TRUE;
			double inflate = pCubeDeserializer.inflate() != null ? pCubeDeserializer.inflate() / 16f : (pBone.getInflate() == null ? 0 : pBone.getInflate() / 16f);
			Vec3 size = RenderUtils.listToVec(pCubeDeserializer.size());
			Vec3 origin = RenderUtils.listToVec(pCubeDeserializer.origin());
			Vec3 rotation = RenderUtils.listToVec(pCubeDeserializer.rotation());
			Vec3 pivot = RenderUtils.listToVec(pCubeDeserializer.pivot());
			origin = new Vec3(-(origin.x + size.x) / 16d, origin.y / 16d, origin.z / 16d);
			Vec3 vertexSize = size.multiply(1 / 16d, 1 / 16d, 1 / 16d);

			pivot = pivot.multiply(-1, 1, 1);
			rotation = new Vec3(Math.toRadians(-rotation.x), Math.toRadians(-rotation.y), Math.toRadians(rotation.z));
			List<QuadData> quads = buildQuads(pCubeDeserializer.uvUnionDeserializer(), new VertexSet(origin, vertexSize, inflate), pCubeDeserializer, pModelDescriptionDeserializer.textureWidth(), pModelDescriptionDeserializer.textureHeight(), mirror);

			return new CubeCache(quads, pivot, rotation, size, inflate, mirror);
		}
	}

	record VertexSet(@NotNull VertexData bottomLeftBack, @NotNull VertexData bottomRightBack,
			@NotNull VertexData topLeftBack,
			@NotNull VertexData topRightBack,
			@NotNull VertexData topLeftFront, @NotNull VertexData topRightFront,
			@NotNull VertexData bottomLeftFront,
			@NotNull VertexData bottomRightFront) {

		public VertexSet(@NotNull Vec3 pOrigin, @NotNull Vec3 pVertexSize, @NotNull Double pInflation) {
			this(
					new VertexData(pOrigin.x - pInflation, pOrigin.y - pInflation, pOrigin.z - pInflation),
					new VertexData(pOrigin.x - pInflation, pOrigin.y - pInflation, pOrigin.z + pVertexSize.z + pInflation),
					new VertexData(pOrigin.x - pInflation, pOrigin.y + pVertexSize.y + pInflation, pOrigin.z - pInflation),
					new VertexData(pOrigin.x - pInflation, pOrigin.y + pVertexSize.y + pInflation, pOrigin.z + pVertexSize.z + pInflation),
					new VertexData(pOrigin.x + pVertexSize.x + pInflation, pOrigin.y + pVertexSize.y + pInflation, pOrigin.z - pInflation),
					new VertexData(pOrigin.x + pVertexSize.x + pInflation, pOrigin.y + pVertexSize.y + pInflation, pOrigin.z + pVertexSize.z + pInflation),
					new VertexData(pOrigin.x + pVertexSize.x + pInflation, pOrigin.y - pInflation, pOrigin.z - pInflation),
					new VertexData(pOrigin.x + pVertexSize.x + pInflation, pOrigin.y - pInflation, pOrigin.z + pVertexSize.z + pInflation));
		}

		public @NotNull List<VertexData> quadWest() {
			return List.of(this.topRightBack, this.topLeftBack, this.bottomLeftBack, this.bottomRightBack);
		}

		public @NotNull List<VertexData> quadEast() {
			return List.of(this.topLeftFront, this.topRightFront, this.bottomRightFront, this.bottomLeftFront);
		}

		public @NotNull List<VertexData> quadNorth() {
			return List.of(this.topLeftBack, this.topLeftFront, this.bottomLeftFront, this.bottomLeftBack);
		}

		public @NotNull List<VertexData> quadSouth() {
			return List.of(this.topRightFront, this.topRightBack, this.bottomRightBack, this.bottomRightFront);
		}

		public @NotNull List<VertexData> quadUp() {
			return List.of(this.topRightBack, this.topRightFront, this.topLeftFront, this.topLeftBack);
		}

		public @NotNull List<VertexData> quadDown() {
			return List.of(this.bottomLeftBack, this.bottomLeftFront, this.bottomRightFront, this.bottomRightBack);
		}

		public @NotNull List<VertexData> verticesForQuad(@NotNull Direction pDirection, boolean pBoxUv, boolean pMirror) {
			return switch (pDirection) {
				case WEST -> new ArrayList<>(pMirror ? quadEast() : quadWest());
				case EAST -> new ArrayList<>(pMirror ? quadWest() : quadEast());
				case NORTH -> new ArrayList<>(quadNorth());
				case SOUTH -> new ArrayList<>(quadSouth());
				case UP -> new ArrayList<>(pMirror && !pBoxUv ? quadDown() : quadUp());
				case DOWN -> new ArrayList<>(pMirror && !pBoxUv ? quadUp() : quadDown());
			};
		}
	}
}
