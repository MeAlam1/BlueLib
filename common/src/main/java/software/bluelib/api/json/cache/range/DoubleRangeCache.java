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
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.json.deserializer.range.DoubleRange;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record DoubleRangeCache(
		@NotNull Double min,
		@NotNull Double max) {

	@NotNull
	public static final Codec<DoubleRangeCache> CODEC =
			NbtCodecUtils.fromNbt(DoubleRangeCache::readFromNBT, DoubleRangeCache::writeToNBT);

	@NotNull
	public static final DataComponentType<DoubleRangeCache> DOUBLE_RANGE_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putDouble("Min", min);
		pTag.putDouble("Max", max);
	}

	@NotNull
	public static DoubleRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new DoubleRangeCache(pTag.getDouble("Min"), pTag.getDouble("Max"));
	}

	public double getRandomValue() {
		return ThreadLocalRandom.current().nextDouble() * (max - min) + min;
	}

	@NotNull
	public static DoubleRangeCache construct(@NotNull DoubleRange pDoubleRange) {
		return new DoubleRangeCache(pDoubleRange.min(), pDoubleRange.max());
	}
}
