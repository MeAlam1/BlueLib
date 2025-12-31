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
import software.bluelib.api.json.deserializer.pos.BlockPos;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record BlockPosCache(
		@NotNull Integer x,
		@NotNull Integer y,
		@NotNull Integer z) {

	@NotNull
	public static final Codec<BlockPosCache> CODEC =
			NbtCodecUtils.fromNbt(BlockPosCache::readFromNBT, BlockPosCache::writeToNBT);

	@NotNull
	public static final DataComponentType<BlockPosCache> BLOCK_POS_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putInt("x", x);
		pTag.putInt("y", y);
		pTag.putInt("z", z);
	}

	@NotNull
	public static BlockPosCache readFromNBT(@NotNull CompoundTag pTag) {
		return new BlockPosCache(pTag.getInt("x"), pTag.getInt("y"), pTag.getInt("z"));
	}

	@NotNull
	public static BlockPosCache construct(@NotNull BlockPos pBlockPos) {
		return new BlockPosCache(pBlockPos.x(), pBlockPos.y(), pBlockPos.z());
	}

	public static net.minecraft.core.BlockPos convert(@NotNull BlockPos pPos) {
		return new net.minecraft.core.BlockPos(pPos.x(), pPos.y(), pPos.z());
	}

	public static net.minecraft.core.BlockPos convert(@NotNull BlockPosCache pPos) {
		return new net.minecraft.core.BlockPos(pPos.x(), pPos.y(), pPos.z());
	}
}
