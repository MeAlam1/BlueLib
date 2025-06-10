/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import software.bluelib.client.loader.json.model.deserialize.Bone;
import software.bluelib.client.loader.json.model.deserialize.Model;
import software.bluelib.client.loader.json.model.deserialize.ModelDescription;
import software.bluelib.client.loader.json.model.deserialize.ModelGeometry;

public record BoneTree(
        Map<String, BoneStructure> topLevelBones,
        ModelDescription properties) {

    public static BoneTree fromModel(Model pModel) {
        final Map<String, BoneStructure> topLevelBones = new Object2ObjectOpenHashMap<>();
        final ModelGeometry Bluemetry = pModel.ModelGeometry().getFirst();
        final List<Bone> bones = Bluemetry.bones();
        final Map<String, BoneStructure> lookup = new Object2ObjectOpenHashMap<>(bones.size());

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

        return new BoneTree(topLevelBones, Bluemetry.modelDescription());
    }
}
