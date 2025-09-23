/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.json.cache.color.RGBAColorCache;
import software.bluelib.api.json.cache.color.RGBColorCache;
import software.bluelib.api.json.cache.pos.BlockPosCache;
import software.bluelib.api.json.cache.pos.Vector2Cache;
import software.bluelib.api.json.cache.pos.Vector3Cache;
import software.bluelib.api.json.cache.range.*;
import software.bluelib.loader.cache.controller.*;
import software.bluelib.loader.cache.variants.EntityCache;
import software.bluelib.loader.cache.variants.VariantCache;

@ApiStatus.Internal
@SuppressWarnings({ "unused" })
public class BlueDataComponentRegistry {

	public static void init() {}

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
	public static final Supplier<DataComponentType<Vector2Cache>> VECTOR_2 = registerData("vector_two", () -> Vector2Cache.VECTOR2_DATA);
	@NotNull
	public static final Supplier<DataComponentType<Vector3Cache>> VECTOR_3 = registerData("vector_three", () -> Vector3Cache.VECTOR3_DATA);
	@NotNull
	public static final Supplier<DataComponentType<BlockPosCache>> BLOCK_POS = registerData("block_pos", () -> BlockPosCache.BLOCK_POS_DATA);

	@NotNull
	public static final Supplier<DataComponentType<RGBAColorCache>> RGBA_COLOR = registerData("rgba_color", () -> RGBAColorCache.RGBA_COLOR_DATA);
	@NotNull
	public static final Supplier<DataComponentType<RGBColorCache>> RGB_COLOR = registerData("rgb_color", () -> RGBColorCache.RGB_COLOR_DATA);

	@NotNull
	public static final Supplier<DataComponentType<EntityCache>> ENTITY_CACHE = registerData("entity_cache", () -> EntityCache.ENTITY_CACHE_DATA);
	@NotNull
	public static final Supplier<DataComponentType<VariantCache>> VARIANT_CACHE = registerData("variant_cache", () -> VariantCache.VARIANT_CACHE_DATA);

	@NotNull
	public static final Supplier<DataComponentType<AnimationCache>> CONTROLLER_ANIMATION_CACHE = registerData("controller_animation_cache", () -> AnimationCache.ANIMATION_CACHE_DATA);
	@NotNull
	public static final Supplier<DataComponentType<StateCache>> STATE_CACHE = registerData("state_cache", () -> StateCache.STATE_CACHE_DATA);
	@NotNull
	public static final Supplier<DataComponentType<BehaviourCache>> BEHAVIOUR_CACHE = registerData("behaviour_cache", () -> BehaviourCache.BEHAVIOUR_CACHE_DATA);
	@NotNull
	public static final Supplier<DataComponentType<GroupCache>> GROUP_CACHE = registerData("group_cache", () -> GroupCache.GROUP_CACHE_DATA);
	@NotNull
	public static final Supplier<DataComponentType<ControllerCache>> CONTROLLER_CACHE = registerData("controller_cache", () -> ControllerCache.CONTROLLER_CACHE_DATA);

	@NotNull
	private static <T> Supplier<DataComponentType<T>> registerData(@NotNull String pId, @NotNull Supplier<DataComponentType<T>> pBuilder) {
		return BlueLibConstants.PlatformHelper.REGISTRY.registerDataComponent(pId, pBuilder);
	}
}
