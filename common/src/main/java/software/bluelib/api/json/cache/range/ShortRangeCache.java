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
import software.bluelib.api.json.deserializer.range.ShortRange;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record ShortRangeCache(
		@NotNull Short min,
		@NotNull Short max) {

	@NotNull
	public static final Codec<ShortRangeCache> CODEC = NbtCodecUtils.fromNbt(ShortRangeCache::readFromNBT, ShortRangeCache::writeToNBT);

	@NotNull
	public static final DataComponentType<ShortRangeCache> SHORT_RANGE_DATA = NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putShort("Min", min);
		pTag.putShort("Max", max);
	}

	@NotNull
	public static ShortRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new ShortRangeCache(pTag.getShort("Min"), pTag.getShort("Max"));
	}

	public short getRandomValue() {
		return (short) (ThreadLocalRandom.current().nextFloat() * (max - min) + min);
	}

	@NotNull
	public static ShortRangeCache construct(@NotNull ShortRange pShortRange) {
		return new ShortRangeCache(pShortRange.min(), pShortRange.max());
	}
}
