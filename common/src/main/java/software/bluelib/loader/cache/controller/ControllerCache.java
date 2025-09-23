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

public record ControllerCache(
		@NotNull String formatVersion,
		@NotNull List<GroupCache> groups) {

	@NotNull
	public static final Codec<ControllerCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(ControllerCache.readFromNBT(tag));
			},
			controllerCache -> {
				CompoundTag tag = new CompoundTag();
				controllerCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	@NotNull
	public static final DataComponentType<ControllerCache> CONTROLLER_CACHE_DATA = DataComponentType.<ControllerCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putString("formatVersion", formatVersion);
		CompoundTagUtils.writeList(
				pTag,
				"groups",
				groups,
				(tag, data) -> {
					data.writeToNBT(tag);
				});
	}

	@NotNull
	public static ControllerCache readFromNBT(@NotNull CompoundTag pTag) {
		String formatVersion = pTag.getString("formatVersion");
		List<GroupCache> groups = CompoundTagUtils.readList(
				pTag,
				"groups",
				GroupCache::readFromNBT);
		return new ControllerCache(formatVersion, groups);
	}

	@NotNull
	public GroupCache getMainGroup() {
		return groups.getFirst();
	}

	@Nullable
	public GroupCache getGroup(@NotNull String pGroupName) {
		for (GroupCache group : groups) {
			if (group.behaviours().containsKey(pGroupName)) {
				return group;
			}
		}
		return null;
	}
}
