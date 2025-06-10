/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.client.loader.cache.model.CubeCache;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.loader.json.model.deserialize.*;
import software.bluelib.client.loader.json.model.object.BoneStructure;
import software.bluelib.client.loader.json.model.object.BoneTree;
import software.bluelib.client.loader.json.model.object.QuadData;
import software.bluelib.client.loader.json.model.object.VertexData;
import software.bluelib.client.utils.RenderUtils;

public interface ModelCacheFactory {

    Map<String, ModelCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
    ModelCacheFactory DEFAULT_FACTORY = new Builtin();

    ModelCache constructBlueModel(BoneTree pBoneTree);

    BoneCache constructBone(BoneStructure pBoneStructure, ModelDescription pModelDescription, @Nullable BoneCache pParent);

    CubeCache constructCube(Cube pCube, ModelDescription pModelDescription, BoneCache pBone);

    default List<QuadData> buildQuads(UVUnion pUvUnion, VertexSet pVertices, Cube pCube, float pTextureWidth, float pTextureHeight, boolean pMirror) {
        List<QuadData> quads = new ArrayList<>(6);

        quads.add(buildQuad(pVertices, pCube, pUvUnion, pTextureWidth, pTextureHeight, pMirror, Direction.WEST));
        quads.add(buildQuad(pVertices, pCube, pUvUnion, pTextureWidth, pTextureHeight, pMirror, Direction.EAST));
        quads.add(buildQuad(pVertices, pCube, pUvUnion, pTextureWidth, pTextureHeight, pMirror, Direction.NORTH));
        quads.add(buildQuad(pVertices, pCube, pUvUnion, pTextureWidth, pTextureHeight, pMirror, Direction.SOUTH));
        quads.add(buildQuad(pVertices, pCube, pUvUnion, pTextureWidth, pTextureHeight, pMirror, Direction.UP));
        quads.add(buildQuad(pVertices, pCube, pUvUnion, pTextureWidth, pTextureHeight, pMirror, Direction.DOWN));

        return quads;
    }

    default QuadData buildQuad(VertexSet pVertices, Cube pCube, UVUnion pUvUnion, float pTextureWidth, float pTextureHeight, boolean pMirror, Direction pDirection) {
        if (!pUvUnion.isBoxUV()) {
            FaceUV faceUV = pUvUnion.faceUV().fromDirection(pDirection);

            if (faceUV == null)
                return null;

            return QuadData.build(pVertices.verticesForQuad(pDirection, false, pMirror || pCube.mirror() == Boolean.TRUE), faceUV.uv(), faceUV.uvSize(),
                    faceUV.uvRotation(), pTextureWidth, pTextureHeight, pMirror, pDirection);
        }

        List<Float> uv = pCube.uvUnion().boxUVCoords();
        List<Float> uvSize = pCube.size();
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

        return QuadData.build(pVertices.verticesForQuad(pDirection, true, pMirror || pCube.mirror() == Boolean.TRUE), uvData.get(0), uvData.get(1), FaceUV.Rotation.NONE, pTextureWidth, pTextureHeight, pMirror, pDirection);
    }

    static ModelCacheFactory getForNamespace(String pNamespace) {
        return FACTORIES.getOrDefault(pNamespace, DEFAULT_FACTORY);
    }

    static void register(String pNamespace, ModelCacheFactory pFactory) {
        FACTORIES.put(pNamespace, pFactory);
    }

    final class Builtin implements ModelCacheFactory {

        @Override
        public ModelCache constructBlueModel(BoneTree pBoneTree) {
            List<BoneCache> bones = new ObjectArrayList<>();

            for (BoneStructure boneStructure : pBoneTree.topLevelBones().values()) {
                bones.add(constructBone(boneStructure, pBoneTree.properties(), null));
            }

            return new ModelCache(bones, pBoneTree.properties());
        }

        @Override
        public BoneCache constructBone(BoneStructure pBoneStructure, ModelDescription pModelDescription, BoneCache pParent) {
            Bone bone = pBoneStructure.self();
            BoneCache newBone = new BoneCache(pParent, bone.name(), bone.mirror(), bone.inflate(), bone.neverRender(), bone.reset());
            Vec3 rotation = RenderUtils.listToVec(bone.rotation());
            Vec3 pivot = RenderUtils.listToVec(bone.pivot());

            newBone.updateRotation((float) Math.toRadians(-rotation.x), (float) Math.toRadians(-rotation.y), (float) Math.toRadians(rotation.z));
            newBone.updatePivot((float) -pivot.x, (float) pivot.y, (float) pivot.z);

            for (Cube cube : bone.cubes()) {
                newBone.getCubes().add(constructCube(cube, pModelDescription, newBone));
            }

            for (BoneStructure child : pBoneStructure.children().values()) {
                newBone.getChildBones().add(constructBone(child, pModelDescription, newBone));
            }

            return newBone;
        }

        @Override
        public CubeCache constructCube(Cube pCube, ModelDescription pModelDescription, BoneCache pBone) {
            boolean mirror = pCube.mirror() == Boolean.TRUE;
            double inflate = pCube.inflate() != null ? pCube.inflate() / 16f : (pBone.getInflate() == null ? 0 : pBone.getInflate() / 16f);
            Vec3 size = RenderUtils.listToVec(pCube.size());
            Vec3 origin = RenderUtils.listToVec(pCube.origin());
            Vec3 rotation = RenderUtils.listToVec(pCube.rotation());
            Vec3 pivot = RenderUtils.listToVec(pCube.pivot());
            origin = new Vec3(-(origin.x + size.x) / 16d, origin.y / 16d, origin.z / 16d);
            Vec3 vertexSize = size.multiply(1 / 16d, 1 / 16d, 1 / 16d);

            pivot = pivot.multiply(-1, 1, 1);
            rotation = new Vec3(Math.toRadians(-rotation.x), Math.toRadians(-rotation.y), Math.toRadians(rotation.z));
            List<QuadData> quads = buildQuads(pCube.uvUnion(), new VertexSet(origin, vertexSize, inflate), pCube, pModelDescription.textureWidth(), pModelDescription.textureHeight(), mirror);

            return new CubeCache(quads, pivot, rotation, size, inflate, mirror);
        }
    }

    record VertexSet(VertexData bottomLeftBack, VertexData bottomRightBack, VertexData topLeftBack,
            VertexData topRightBack,
            VertexData topLeftFront, VertexData topRightFront, VertexData bottomLeftFront,
            VertexData bottomRightFront) {

        public VertexSet(Vec3 pOrigin, Vec3 pVertexSize, double pInflation) {
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

        public List<VertexData> quadWest() {
            return List.of(this.topRightBack, this.topLeftBack, this.bottomLeftBack, this.bottomRightBack);
        }

        public List<VertexData> quadEast() {
            return List.of(this.topLeftFront, this.topRightFront, this.bottomRightFront, this.bottomLeftFront);
        }

        public List<VertexData> quadNorth() {
            return List.of(this.topLeftBack, this.topLeftFront, this.bottomLeftFront, this.bottomLeftBack);
        }

        public List<VertexData> quadSouth() {
            return List.of(this.topRightFront, this.topRightBack, this.bottomRightBack, this.bottomRightFront);
        }

        public List<VertexData> quadUp() {
            return List.of(this.topRightBack, this.topRightFront, this.topLeftFront, this.topLeftBack);
        }

        public List<VertexData> quadDown() {
            return List.of(this.bottomLeftBack, this.bottomLeftFront, this.bottomRightFront, this.bottomRightBack);
        }

        public List<VertexData> verticesForQuad(Direction pDirection, boolean pBoxUv, boolean pMirror) {
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
