/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.model.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.json.deserialize.model.BoneDeserializer;
import software.bluelib.loader.json.deserialize.model.ModelDeserializer;
import software.bluelib.loader.json.deserialize.model.ModelDescriptionDeserializer;
import software.bluelib.loader.json.deserialize.model.ModelGeometryDeserializer;

public record BoneTree(
		@NotNull Map<String, BoneStructure> topLevelBones,
		@Nullable ModelDescriptionDeserializer description) {

	public static @NotNull BoneTree fromModel(@NotNull ModelDeserializer pModelDeserializer) {
		final Map<String, BoneStructure> topLevelBones = new Object2ObjectOpenHashMap<>();
		final ModelGeometryDeserializer geometry = pModelDeserializer.ModelGeometryDeserializer().getFirst();
		final List<BoneDeserializer> boneDeserializers = geometry.boneDeserializers();
		final Map<String, BoneStructure> lookup = new Object2ObjectOpenHashMap<>(boneDeserializers.size());

		for (BoneDeserializer boneDeserializer : boneDeserializers) {
			final BoneStructure boneStructure = new BoneStructure(boneDeserializer);

			lookup.put(boneDeserializer.name(), boneStructure);

			if (boneDeserializer.parent() == null)
				topLevelBones.put(boneDeserializer.name(), boneStructure);
		}

		for (BoneDeserializer boneDeserializer : boneDeserializers) {
			final String parentName = boneDeserializer.parent();

			if (parentName != null) {
				final String boneName = boneDeserializer.name();

				if (parentName.equals(boneName))
					throw new IllegalArgumentException("Invalid model definition. Bone has defined itself as its own parent: " + boneName);

				final BoneStructure parentStructure = lookup.get(parentName);

				if (parentStructure == null)
					throw new IllegalArgumentException("Invalid model definition. Found bone with undefined parent (child -> parent): " + boneName + " -> " + parentName);

				parentStructure.children().put(boneName, lookup.get(boneName));
			}
		}

		return new BoneTree(topLevelBones, geometry.modelDescriptionDeserializer());
	}
}
