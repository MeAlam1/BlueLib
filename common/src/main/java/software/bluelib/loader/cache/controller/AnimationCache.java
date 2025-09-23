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
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.minecraft.CompoundTagUtils;

public record AnimationCache(
		@NotNull List<String> conditions,
		@NotNull String animation,
		@Nullable Integer priority,
		@Nullable String sound) {

	@NotNull
	public static final Codec<AnimationCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(AnimationCache.readFromNBT(tag));
			},
			animationCache -> {
				CompoundTag tag = new CompoundTag();
				animationCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	@NotNull
	public static final DataComponentType<AnimationCache> ANIMATION_CACHE_DATA = DataComponentType.<AnimationCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		CompoundTagUtils.writeList(
				pTag,
				"conditions",
				conditions,
				(tag, data) -> tag.putString("condition", data));
		pTag.putString("animation", animation);
		if (priority != null) {
			pTag.putInt("priority", priority);
		}
		if (sound != null) {
			pTag.putString("sound", sound);
		}
	}

	@NotNull
	public static AnimationCache readFromNBT(@NotNull CompoundTag pTag) {
		List<String> conditions = CompoundTagUtils.readList(
				pTag,
				"conditions",
				tag -> tag.getString("condition"));
		String animation = pTag.getString("animation");
		Integer priority = pTag.contains("priority") ? pTag.getInt("priority") : null;
		String sound = pTag.contains("sound") ? pTag.getString("sound") : null;
		return new AnimationCache(conditions, animation, priority, sound);
	}
}
