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
import software.bluelib.api.json.deserializer.range.ByteRange;

public record ByteRangeCache(
		@NotNull Byte min,
		@NotNull Byte max) {

	public static final Codec<ByteRangeCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(ByteRangeCache.readFromNBT(tag));
			},
			byteRangeCache -> {
				CompoundTag tag = new CompoundTag();
				byteRangeCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	public static final DataComponentType<ByteRangeCache> BYTE_RANGE_DATA = DataComponentType.<ByteRangeCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putByte("Min", min);
		pTag.putByte("Max", max);
	}

	@NotNull
	public static ByteRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new ByteRangeCache(pTag.getByte("Min"), pTag.getByte("Max"));
	}

	@NotNull
	public static ByteRangeCache construct(@NotNull ByteRange pByteRange) {
		return new ByteRangeCache(pByteRange.min(), pByteRange.max());
	}
}
