/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.object;

import java.util.List;
import java.util.Optional;
import software.bluelib.loader.loading.json.raw.ModelProperties;

public record BakedGeoModel(List<GeoBone> topLevelBones, ModelProperties properties) {

    public Optional<GeoBone> getBone(String name) {
        for (GeoBone bone : this.topLevelBones) {
            GeoBone childBone = searchForChildBone(bone, name);

            if (childBone != null)
                return Optional.of(childBone);
        }

        return Optional.empty();
    }

    public GeoBone searchForChildBone(GeoBone parent, String name) {
        if (parent.getName().equals(name))
            return parent;

        for (GeoBone bone : parent.getChildBones()) {
            if (bone.getName().equals(name))
                return bone;

            GeoBone subChildBone = searchForChildBone(bone, name);

            if (subChildBone != null)
                return subChildBone;
        }

        return null;
    }
}
