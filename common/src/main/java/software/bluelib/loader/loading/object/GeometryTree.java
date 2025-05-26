/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.loading.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bluelib.loader.loading.json.raw.Bone;
import software.bluelib.loader.loading.json.raw.MinecraftGeometry;
import software.bluelib.loader.loading.json.raw.Model;
import software.bluelib.loader.loading.json.raw.ModelProperties;

public record GeometryTree(Map<String, BoneStructure> topLevelBones, ModelProperties properties) {

    public static GeometryTree fromModel(Model model) {
        final Map<String, BoneStructure> topLevelBones = new Object2ObjectOpenHashMap<>();
        final MinecraftGeometry geometry = model.minecraftGeometry()[0];
        final Bone[] bones = geometry.bones();
        final Map<String, BoneStructure> lookup = new Object2ObjectOpenHashMap<>(bones.length);

        for (Bone bone : bones) {
            final BoneStructure boneStructure = new BoneStructure(bone);

            lookup.put(bone.name(), boneStructure);

            if (bone.parent() == null)
                topLevelBones.put(bone.name(), boneStructure);
        }

        for (Bone bone : bones) {
            final String parentName = bone.parent();

            if (parentName != null) {
                final String boneName = bone.name();

                if (parentName.equals(boneName))
                    throw new IllegalArgumentException("Invalid model definition. Bone has defined itself as its own parent: " + boneName);

                final BoneStructure parentStructure = lookup.get(parentName);

                if (parentStructure == null)
                    throw new IllegalArgumentException("Invalid model definition. Found bone with undefined parent (child -> parent): " + boneName + " -> " + parentName);

                parentStructure.children().put(boneName, lookup.get(boneName));
            }
        }

        return new GeometryTree(topLevelBones, geometry.modelProperties());
    }
}
