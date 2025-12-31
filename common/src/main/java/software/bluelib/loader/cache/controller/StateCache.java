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

import java.util.List;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.codec.NbtCodecUtils;
import software.bluelib.api.utils.minecraft.CompoundTagUtils;

public record StateCache(
		boolean isOverlay,
		@NotNull List<AnimationCache> animations) {

	@NotNull
	public static final Codec<StateCache> CODEC =
			NbtCodecUtils.fromNbt(StateCache::readFromNBT, StateCache::writeToNBT);

	@NotNull
	public static final DataComponentType<StateCache> STATE_CACHE_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putBoolean("isOverlay", isOverlay);
		CompoundTagUtils.writeList(
				pTag,
				"animations",
				animations,
				(tag, data) -> {
					data.writeToNBT(tag);
				});
	}

	@NotNull
	public static StateCache readFromNBT(@NotNull CompoundTag pTag) {
		boolean isOverlay = pTag.getBoolean("isOverlay");
		List<AnimationCache> animations = CompoundTagUtils.readList(
				pTag,
				"animations",
				AnimationCache::readFromNBT);
		return new StateCache(isOverlay, animations);
	}
}
