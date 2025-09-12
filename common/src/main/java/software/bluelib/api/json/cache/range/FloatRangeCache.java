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
import software.bluelib.api.json.deserializer.range.FloatRange;

public record FloatRangeCache(
		@NotNull Float min,
		@NotNull Float max) {

	public static final Codec<FloatRangeCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(FloatRangeCache.readFromNBT(tag));
			},
			floatRangeCache -> {
				CompoundTag tag = new CompoundTag();
				floatRangeCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	public static final DataComponentType<FloatRangeCache> FLOAT_RANGE_DATA = DataComponentType.<FloatRangeCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putFloat("Min", min);
		pTag.putFloat("Max", max);
	}

	@NotNull
	public static FloatRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new FloatRangeCache(pTag.getFloat("Min"), pTag.getFloat("Max"));
	}

	public float getRandomValue() {
		return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
	}

	@NotNull
	public static FloatRangeCache construct(@NotNull FloatRange pFloatRange) {
		return new FloatRangeCache(pFloatRange.min(), pFloatRange.max());
	}
}
