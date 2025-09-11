/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.json.cache.range.*;

import java.util.function.Supplier;

@ApiStatus.Internal
@SuppressWarnings({"unused"})
public class BlueDataComponentRegistry {

	public static void init() {
	}

	@NotNull
	public static final Supplier<DataComponentType<Long>> STACK_ANIMATABLE_ID = registerData("stack_animatable_id", () -> DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).build());

	@NotNull
	public static final Supplier<DataComponentType<IntRangeCache>> INT_RANGE = registerData("int_range", () -> IntRangeCache.INT_RANGE_DATA);

	@NotNull
	public static final Supplier<DataComponentType<ByteRangeCache>> BYTE_RANGE = registerData("byte_range", () -> ByteRangeCache.BYTE_RANGE_DATA);

	@NotNull
	public static final Supplier<DataComponentType<DoubleRangeCache>> DOUBLE_RANGE = registerData("double_range", () -> DoubleRangeCache.DOUBLE_RANGE_DATA);

	@NotNull
	public static final Supplier<DataComponentType<FloatRangeCache>> FLOAT_RANGE = registerData("float_range", () -> FloatRangeCache.FLOAT_RANGE_DATA);

	@NotNull
	public static final Supplier<DataComponentType<LongRangeCache>> LONG_RANGE = registerData("long_range", () -> LongRangeCache.LONG_RANGE_DATA);

	@NotNull
	public static final Supplier<DataComponentType<ShortRangeCache>> SHORT_RANGE = registerData("short_range", () -> ShortRangeCache.SHORT_RANGE_DATA);

	@NotNull
	private static <T> Supplier<DataComponentType<T>> registerData(@NotNull String pId, @NotNull Supplier<DataComponentType<T>> pBuilder) {
		return BlueLibConstants.PlatformHelper.REGISTRY.registerDataComponent(pId, pBuilder);
	}
}
