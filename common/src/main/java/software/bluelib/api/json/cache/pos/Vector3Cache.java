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
import org.joml.Vector3f;
import software.bluelib.api.json.deserializer.pos.Vector3;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record Vector3Cache(
		@NotNull Float x,
		@NotNull Float y,
		@NotNull Float z) {

	@NotNull
	public static final Codec<Vector3Cache> CODEC =
			NbtCodecUtils.fromNbt(Vector3Cache::readFromNBT, Vector3Cache::writeToNBT);

	@NotNull
	public static final DataComponentType<Vector3Cache> VECTOR3_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putFloat("x", x);
		pTag.putFloat("y", y);
		pTag.putFloat("z", z);
	}

	@NotNull
	public static Vector3Cache readFromNBT(@NotNull CompoundTag pTag) {
		return new Vector3Cache(pTag.getFloat("x"), pTag.getFloat("y"), pTag.getFloat("z"));
	}

	@NotNull
	public static Vector3Cache construct(@NotNull Vector3 pVectorRange) {
		return new Vector3Cache(pVectorRange.x(), pVectorRange.y(), pVectorRange.z());
	}

	public static Vector3f convert(@NotNull Vector3 pVector) {
		return new Vector3f(pVector.x(), pVector.y(), pVector.z());
	}

	public static Vector3f convert(@NotNull Vector3Cache pVector) {
		return new Vector3f(pVector.x(), pVector.y(), pVector.z());
	}
}
