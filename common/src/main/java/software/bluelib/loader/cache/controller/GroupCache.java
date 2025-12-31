/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.controller;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import java.util.Map;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.codec.NbtCodecUtils;
import software.bluelib.api.utils.minecraft.CompoundTagUtils;

public record GroupCache(
		@NotNull Map<String, BehaviourCache> behaviours) {

	@NotNull
	public static final Codec<GroupCache> CODEC =
			NbtCodecUtils.fromNbt(GroupCache::readFromNBT, GroupCache::writeToNBT);

	@NotNull
	public static final DataComponentType<GroupCache> GROUP_CACHE_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		CompoundTagUtils.writeMap(
				pTag,
				"behaviours",
				behaviours,
				(tag, key) -> tag.putString("name", key),
				(tag, value) -> value.writeToNBT(tag),
				"name",
				"behaviour");
	}

	@NotNull
	public static GroupCache readFromNBT(@NotNull CompoundTag pTag) {
		Map<String, BehaviourCache> behaviours = CompoundTagUtils.readMap(
				pTag,
				"behaviours",
				tag -> tag.getString("name"),
				BehaviourCache::readFromNBT,
				"name",
				"behaviour");
		return new GroupCache(behaviours);
	}

	@NotNull
	public BehaviourCache getBehaviour(@NotNull String pName) {
		return behaviours.get(pName);
	}
}
