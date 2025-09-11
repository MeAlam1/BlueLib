/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.cache.pos;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.json.deserializer.pos.Vector2;

public record Vector2Cache(
		@NotNull Float x,
		@NotNull Float y) {

	public static final Codec<Vector2Cache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(Vector2Cache.readFromNBT(tag));
			},
			vector2Cache -> {
				CompoundTag tag = new CompoundTag();
				vector2Cache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	public static final DataComponentType<Vector2Cache> VECTOR2_DATA = DataComponentType.<Vector2Cache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putFloat("x", x);
		pTag.putFloat("y", y);
	}

	@NotNull
	public static Vector2Cache readFromNBT(@NotNull CompoundTag pTag) {
		return new Vector2Cache(pTag.getFloat("x"), pTag.getFloat("y"));
	}

	@NotNull
	public static Vector2Cache construct(@NotNull Vector2 pVectorRange) {
		return new Vector2Cache(pVectorRange.x(), pVectorRange.y());
	}
}
