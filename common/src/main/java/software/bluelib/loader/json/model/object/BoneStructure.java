/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.model.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.json.deserialize.model.Bone;

public record BoneStructure(
		@NotNull Bone self,
		@NotNull Map<String, BoneStructure> children) {

	public BoneStructure(@NotNull Bone pSelf) {
		this(pSelf, new Object2ObjectOpenHashMap<>());
	}
}
