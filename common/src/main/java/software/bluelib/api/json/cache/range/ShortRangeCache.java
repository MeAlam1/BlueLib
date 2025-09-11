/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.cache.range;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.json.deserializer.range.ShortRange;

public record ShortRangeCache(
		@NotNull Short min,
		@NotNull Short max) {

	public static final Codec<ShortRangeCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(ShortRangeCache.readFromNBT(tag));
			},
			shortRangeCache -> {
				CompoundTag tag = new CompoundTag();
				shortRangeCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	public static final DataComponentType<ShortRangeCache> SHORT_RANGE_DATA = DataComponentType.<ShortRangeCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putShort("Min", min);
		pTag.putShort("Max", max);
	}

	@NotNull
	public static ShortRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new ShortRangeCache(pTag.getShort("Min"), pTag.getShort("Max"));
	}

	@NotNull
	public static ShortRangeCache construct(@NotNull ShortRange pShortRange) {
		return new ShortRangeCache(pShortRange.min(), pShortRange.max());
	}
}
