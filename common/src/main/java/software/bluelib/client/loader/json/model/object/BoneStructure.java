/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bluelib.client.loader.json.model.deserialize.Bone;

public record BoneStructure(
        Bone self,
        Map<String, BoneStructure> children) {

    public BoneStructure(Bone self) {
        this(self, new Object2ObjectOpenHashMap<>());
    }
}
