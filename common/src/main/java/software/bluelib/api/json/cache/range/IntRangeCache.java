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
import software.bluelib.api.json.deserializer.range.IntRange;

public record IntRangeCache(
		@NotNull Integer min,
		@NotNull Integer max) {

	@NotNull
	public static final Codec<IntRangeCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(IntRangeCache.readFromNBT(tag));
			},
			intRangeCache -> {
				CompoundTag tag = new CompoundTag();
				intRangeCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	@NotNull
	public static final DataComponentType<IntRangeCache> INT_RANGE_DATA = DataComponentType.<IntRangeCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putInt("Min", min);
		pTag.putInt("Max", max);
	}

	@NotNull
	public static IntRangeCache readFromNBT(@NotNull CompoundTag pTag) {
		return new IntRangeCache(pTag.getInt("Min"), pTag.getInt("Max"));
	}

	public int getRandomValue() {
		return ThreadLocalRandom.current().nextInt() * (max - min) + min;
	}

	@NotNull
	public static IntRangeCache construct(@NotNull IntRange pIntRange) {
		return new IntRangeCache(pIntRange.min(), pIntRange.max());
	}
}
