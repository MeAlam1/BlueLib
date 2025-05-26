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

public record BoneStructure(Bone self, Map<String, BoneStructure> children) {

    public BoneStructure(Bone self) {
        this(self, new Object2ObjectOpenHashMap<>());
    }
}
