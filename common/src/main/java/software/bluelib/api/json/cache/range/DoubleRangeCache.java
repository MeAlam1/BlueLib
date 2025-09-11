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
import software.bluelib.api.json.deserializer.range.DoubleRange;

public record DoubleRangeCache(
		@NotNull Double min,
		@NotNull Double max) {

	public static final Codec<DoubleRangeCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(DoubleRangeCache.readFromNBT(tag));
			},
			doubleRangeCache -> {
				CompoundTag tag = new CompoundTag();
				doubleRangeCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	public static final DataComponentType<DoubleRangeCache> DOUBLE_RANGE_DATA = DataComponentType.<DoubleRangeCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putDouble("Min", min);
		pTag.putDouble("Max", max);
	}

	@NotNull
	public static DoubleRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new DoubleRangeCache(pTag.getDouble("Min"), pTag.getDouble("Max"));
	}

	@NotNull
	public static DoubleRangeCache construct(@NotNull DoubleRange pDoubleRange) {
		return new DoubleRangeCache(pDoubleRange.min(), pDoubleRange.max());
	}
}
