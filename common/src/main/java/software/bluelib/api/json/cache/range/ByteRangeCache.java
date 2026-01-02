/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.cache.range;

import com.mojang.serialization.Codec;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.json.deserializer.range.ByteRange;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record ByteRangeCache(
		@NotNull Byte min,
		@NotNull Byte max) {

	@NotNull
	public static final Codec<ByteRangeCache> CODEC = NbtCodecUtils.fromNbt(ByteRangeCache::readFromNBT, ByteRangeCache::writeToNBT);

	@NotNull
	public static final DataComponentType<ByteRangeCache> BYTE_RANGE_DATA = NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putByte("Min", min);
		pTag.putByte("Max", max);
	}

	@NotNull
	public static ByteRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new ByteRangeCache(pTag.getByte("Min"), pTag.getByte("Max"));
	}

	public byte getRandomValue() {
		return (byte) (ThreadLocalRandom.current().nextInt(min, max + 1));
	}

	@NotNull
	public static ByteRangeCache construct(@NotNull ByteRange pByteRange) {
		return new ByteRangeCache(pByteRange.min(), pByteRange.max());
	}
}
