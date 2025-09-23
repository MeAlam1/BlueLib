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
import software.bluelib.api.json.deserializer.range.LongRange;

public record LongRangeCache(
		@NotNull Long min,
		@NotNull Long max) {

	@NotNull
	public static final Codec<LongRangeCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(LongRangeCache.readFromNBT(tag));
			},
			longRangeCache -> {
				CompoundTag tag = new CompoundTag();
				longRangeCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	@NotNull
	public static final DataComponentType<LongRangeCache> LONG_RANGE_DATA = DataComponentType.<LongRangeCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putLong("Min", min);
		pTag.putLong("Max", max);
	}

	@NotNull
	public static LongRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new LongRangeCache(pTag.getLong("Min"), pTag.getLong("Max"));
	}

	public long getRandomValue() {
		return ThreadLocalRandom.current().nextLong() * (max - min) + min;
	}

	@NotNull
	public static LongRangeCache construct(@NotNull LongRange pLongRange) {
		return new LongRangeCache(pLongRange.min(), pLongRange.max());
	}
}
