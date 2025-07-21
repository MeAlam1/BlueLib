/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.model;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.json.deserialize.model.ModelDescription;

public record ModelCache(@NotNull List<BoneCache> topLevelBones, @NotNull ModelDescription modelDescription) {

	@NotNull
	public Optional<BoneCache> getBone(@NotNull String pName) {
		for (BoneCache bone : this.topLevelBones) {
			BoneCache childBone = searchForChildBone(bone, pName);

			if (childBone != null)
				return Optional.of(childBone);
		}

		return Optional.empty();
	}

	@Nullable
	public BoneCache searchForChildBone(@NotNull BoneCache pParent, @NotNull String pName) {
		if (pParent.getName().equals(pName))
			return pParent;

		for (BoneCache bone : pParent.getChildBones()) {
			if (bone.getName().equals(pName))
				return bone;

			BoneCache subChildBone = searchForChildBone(bone, pName);

			if (subChildBone != null)
				return subChildBone;
		}

		return null;
	}
}
