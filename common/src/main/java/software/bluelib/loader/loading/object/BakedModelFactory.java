/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.loading.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.object.*;
import software.bluelib.loader.loading.json.raw.*;
import software.bluelib.loader.util.RenderUtil;

public interface BakedModelFactory {

    Map<String, BakedModelFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
    BakedModelFactory DEFAULT_FACTORY = new Builtin();

    BakedGeoModel constructGeoModel(GeometryTree geometryTree);

    GeoBone constructBone(BoneStructure boneStructure, ModelProperties properties, @Nullable GeoBone parent);

    GeoCube constructCube(Cube cube, ModelProperties properties, GeoBone bone);

    default GeoQuad[] buildQuads(UVUnion uvUnion, VertexSet vertices, Cube cube, float textureWidth, float textureHeight, boolean mirror) {
        GeoQuad[] quads = new GeoQuad[6];

        quads[0] = buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.WEST);
        quads[1] = buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.EAST);
        quads[2] = buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.NORTH);
        quads[3] = buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.SOUTH);
        quads[4] = buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.UP);
        quads[5] = buildQuad(vertices, cube, uvUnion, textureWidth, textureHeight, mirror, Direction.DOWN);

        return quads;
    }

    default GeoQuad buildQuad(VertexSet vertices, Cube cube, UVUnion uvUnion, float textureWidth, float textureHeight, boolean mirror, Direction direction) {
        if (!uvUnion.isBoxUV()) {
            FaceUV faceUV = uvUnion.faceUV().fromDirection(direction);

            if (faceUV == null)
                return null;

            return GeoQuad.build(vertices.verticesForQuad(direction, false, mirror || cube.mirror() == Boolean.TRUE), faceUV.uv(), faceUV.uvSize(),
                    faceUV.uvRotation(), textureWidth, textureHeight, mirror, direction);
        }

        double[] uv = cube.uv().boxUVCoords();
        double[] uvSize = cube.size();
        Vec3 uvSizeVec = new Vec3(Math.floor(uvSize[0]), Math.floor(uvSize[1]), Math.floor(uvSize[2]));
        double[][] uvData = switch (direction) {
            case WEST -> new double[][] {
                    new double[] { uv[0] + uvSizeVec.z + uvSizeVec.x, uv[1] + uvSizeVec.z },
                    new double[] { uvSizeVec.z, uvSizeVec.y }
            };
            case EAST -> new double[][] {
                    new double[] { uv[0], uv[1] + uvSizeVec.z },
                    new double[] { uvSizeVec.z, uvSizeVec.y }
            };
            case NORTH -> new double[][] {
                    new double[] { uv[0] + uvSizeVec.z, uv[1] + uvSizeVec.z },
                    new double[] { uvSizeVec.x, uvSizeVec.y }
            };
            case SOUTH -> new double[][] {
                    new double[] { uv[0] + uvSizeVec.z + uvSizeVec.x + uvSizeVec.z, uv[1] + uvSizeVec.z },
                    new double[] { uvSizeVec.x, uvSizeVec.y }
            };
            case UP -> new double[][] {
                    new double[] { uv[0] + uvSizeVec.z, uv[1] },
                    new double[] { uvSizeVec.x, uvSizeVec.z }
            };
            case DOWN -> new double[][] {
                    new double[] { uv[0] + uvSizeVec.z + uvSizeVec.x, uv[1] + uvSizeVec.z },
                    new double[] { uvSizeVec.x, -uvSizeVec.z }
            };
        };

        return GeoQuad.build(vertices.verticesForQuad(direction, true, mirror || cube.mirror() == Boolean.TRUE), uvData[0], uvData[1], FaceUV.Rotation.NONE, textureWidth, textureHeight, mirror, direction);
    }

    static BakedModelFactory getForNamespace(String namespace) {
        return FACTORIES.getOrDefault(namespace, DEFAULT_FACTORY);
    }

    static void register(String namespace, BakedModelFactory factory) {
        FACTORIES.put(namespace, factory);
    }

    final class Builtin implements BakedModelFactory {

        @Override
        public BakedGeoModel constructGeoModel(GeometryTree geometryTree) {
            List<GeoBone> bones = new ObjectArrayList<>();

            for (BoneStructure boneStructure : geometryTree.topLevelBones().values()) {
                bones.add(constructBone(boneStructure, geometryTree.properties(), null));
            }

            return new BakedGeoModel(bones, geometryTree.properties());
        }

        @Override
        public GeoBone constructBone(BoneStructure boneStructure, ModelProperties properties, GeoBone parent) {
            Bone bone = boneStructure.self();
            GeoBone newBone = new GeoBone(parent, bone.name(), bone.mirror(), bone.inflate(), bone.neverRender(), bone.reset());
            Vec3 rotation = RenderUtil.arrayToVec(bone.rotation());
            Vec3 pivot = RenderUtil.arrayToVec(bone.pivot());

            newBone.updateRotation((float) Math.toRadians(-rotation.x), (float) Math.toRadians(-rotation.y), (float) Math.toRadians(rotation.z));
            newBone.updatePivot((float) -pivot.x, (float) pivot.y, (float) pivot.z);

            for (Cube cube : bone.cubes()) {
                newBone.getCubes().add(constructCube(cube, properties, newBone));
            }

            for (BoneStructure child : boneStructure.children().values()) {
                newBone.getChildBones().add(constructBone(child, properties, newBone));
            }

            return newBone;
        }

        @Override
        public GeoCube constructCube(Cube cube, ModelProperties properties, GeoBone bone) {
            boolean mirror = cube.mirror() == Boolean.TRUE;
            double inflate = cube.inflate() != null ? cube.inflate() / 16f : (bone.getInflate() == null ? 0 : bone.getInflate() / 16f);
            Vec3 size = RenderUtil.arrayToVec(cube.size());
            Vec3 origin = RenderUtil.arrayToVec(cube.origin());
            Vec3 rotation = RenderUtil.arrayToVec(cube.rotation());
            Vec3 pivot = RenderUtil.arrayToVec(cube.pivot());
            origin = new Vec3(-(origin.x + size.x) / 16d, origin.y / 16d, origin.z / 16d);
            Vec3 vertexSize = size.multiply(1 / 16d, 1 / 16d, 1 / 16d);

            pivot = pivot.multiply(-1, 1, 1);
            rotation = new Vec3(Math.toRadians(-rotation.x), Math.toRadians(-rotation.y), Math.toRadians(rotation.z));
            GeoQuad[] quads = buildQuads(cube.uv(), new VertexSet(origin, vertexSize, inflate), cube, (float) properties.textureWidth(), (float) properties.textureHeight(), mirror);

            return new GeoCube(quads, pivot, rotation, size, inflate, mirror);
        }
    }

    record VertexSet(GeoVertex bottomLeftBack, GeoVertex bottomRightBack, GeoVertex topLeftBack, GeoVertex topRightBack,
            GeoVertex topLeftFront, GeoVertex topRightFront, GeoVertex bottomLeftFront, GeoVertex bottomRightFront) {

        public VertexSet(Vec3 origin, Vec3 vertexSize, double inflation) {
            this(
                    new GeoVertex(origin.x - inflation, origin.y - inflation, origin.z - inflation),
                    new GeoVertex(origin.x - inflation, origin.y - inflation, origin.z + vertexSize.z + inflation),
                    new GeoVertex(origin.x - inflation, origin.y + vertexSize.y + inflation, origin.z - inflation),
                    new GeoVertex(origin.x - inflation, origin.y + vertexSize.y + inflation, origin.z + vertexSize.z + inflation),
                    new GeoVertex(origin.x + vertexSize.x + inflation, origin.y + vertexSize.y + inflation, origin.z - inflation),
                    new GeoVertex(origin.x + vertexSize.x + inflation, origin.y + vertexSize.y + inflation, origin.z + vertexSize.z + inflation),
                    new GeoVertex(origin.x + vertexSize.x + inflation, origin.y - inflation, origin.z - inflation),
                    new GeoVertex(origin.x + vertexSize.x + inflation, origin.y - inflation, origin.z + vertexSize.z + inflation));
        }

        public GeoVertex[] quadWest() {
            return new GeoVertex[] { this.topRightBack, this.topLeftBack, this.bottomLeftBack, this.bottomRightBack };
        }

        public GeoVertex[] quadEast() {
            return new GeoVertex[] { this.topLeftFront, this.topRightFront, this.bottomRightFront, this.bottomLeftFront };
        }

        public GeoVertex[] quadNorth() {
            return new GeoVertex[] { this.topLeftBack, this.topLeftFront, this.bottomLeftFront, this.bottomLeftBack };
        }

        public GeoVertex[] quadSouth() {
            return new GeoVertex[] { this.topRightFront, this.topRightBack, this.bottomRightBack, this.bottomRightFront };
        }

        public GeoVertex[] quadUp() {
            return new GeoVertex[] { this.topRightBack, this.topRightFront, this.topLeftFront, this.topLeftBack };
        }

        public GeoVertex[] quadDown() {
            return new GeoVertex[] { this.bottomLeftBack, this.bottomLeftFront, this.bottomRightFront, this.bottomRightBack };
        }

        public GeoVertex[] verticesForQuad(Direction direction, boolean boxUv, boolean mirror) {
            return switch (direction) {
                case WEST -> mirror ? quadEast() : quadWest();
                case EAST -> mirror ? quadWest() : quadEast();
                case NORTH -> quadNorth();
                case SOUTH -> quadSouth();
                case UP -> mirror && !boxUv ? quadDown() : quadUp();
                case DOWN -> mirror && !boxUv ? quadUp() : quadDown();
            };
        }
    }
}
