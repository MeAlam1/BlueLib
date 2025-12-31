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
import org.joml.Vector2f;
import software.bluelib.api.json.deserializer.pos.Vector2;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record Vector2Cache(
		@NotNull Float x,
		@NotNull Float y) {

	@NotNull
	public static final Codec<Vector2Cache> CODEC =
			NbtCodecUtils.fromNbt(Vector2Cache::readFromNBT, Vector2Cache::writeToNBT);

	@NotNull
	public static final DataComponentType<Vector2Cache> VECTOR2_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

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

	public static Vector2f convert(@NotNull Vector2 pVector) {
		return new Vector2f(pVector.x(), pVector.y());
	}

	public static Vector2f convert(@NotNull Vector2Cache pVector) {
		return new Vector2f(pVector.x(), pVector.y());
	}
}
