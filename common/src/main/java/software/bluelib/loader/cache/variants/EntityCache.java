/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.variants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import java.util.Map;
import java.util.Set;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.codec.NbtCodecUtils;
import software.bluelib.api.utils.minecraft.CompoundTagUtils;
import software.bluelib.loader.cache.controller.StateCache;

public record EntityCache(
		@NotNull String formatVersion,
		@NotNull Map<String, VariantCache> variants) {

	@NotNull
	public static final Codec<EntityCache> CODEC =
			NbtCodecUtils.fromNbt(EntityCache::readFromNBT, EntityCache::writeToNBT);

	@NotNull
	public static final DataComponentType<EntityCache> ENTITY_CACHE_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putString("formatVersion", formatVersion);
		CompoundTagUtils.writeMap(
				pTag,
				"variants",
				variants,
				(tag, key) -> tag.putString("name", key),
				(tag, value) -> value.writeToNBT(tag),
				"name",
				"data");
	}

	@NotNull
	public static EntityCache readFromNBT(@NotNull CompoundTag pTag) {
		String formatVersion = pTag.getString("formatVersion");
		Map<String, VariantCache> variants = CompoundTagUtils.readMap(
				pTag,
				"variants",
				tag -> tag.getString("name"),
				VariantCache::readFromNBT,
				"name",
				"data");
		return new EntityCache(formatVersion, variants);
	}

	@NotNull
	public Set<String> getVariantNames() {
		return variants.keySet();
	}

	@Nullable
	public VariantCache getVariant(@NotNull String pName) {
		return variants.get(pName);
	}
}
