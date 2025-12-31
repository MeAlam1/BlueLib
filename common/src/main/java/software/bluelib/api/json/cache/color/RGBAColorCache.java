/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json.cache.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import java.awt.*;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.json.deserializer.color.RGBAColor;
import software.bluelib.api.utils.codec.NbtCodecUtils;

public record RGBAColorCache(
		@NotNull Integer red,
		@NotNull Integer green,
		@NotNull Integer blue,
		@NotNull Integer alpha) {

	@NotNull
	public static final Codec<RGBAColorCache> CODEC =
			NbtCodecUtils.fromNbt(RGBAColorCache::readFromNBT, RGBAColorCache::writeToNBT);

	@NotNull
	public static final DataComponentType<RGBAColorCache> RGBA_COLOR_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putInt("Red", red);
		pTag.putInt("Green", green);
		pTag.putInt("Blue", blue);
		pTag.putInt("Alpha", alpha);
	}

	@NotNull
	public static RGBAColorCache readFromNBT(@NotNull CompoundTag pTag) {
		return new RGBAColorCache(pTag.getInt("Red"), pTag.getInt("Green"), pTag.getInt("Blue"), pTag.getInt("Alpha"));
	}

	@NotNull
	public static RGBAColorCache construct(@NotNull RGBAColor pColor) {
		return new RGBAColorCache(pColor.red(), pColor.green(), pColor.blue(), pColor.alpha());
	}

	public static Color convert(@NotNull RGBAColor pColor) {
		return new Color(pColor.red(), pColor.green(), pColor.blue(), pColor.alpha());
	}

	public static Color convert(@NotNull RGBAColorCache pColor) {
		return new Color(pColor.red(), pColor.green(), pColor.blue(), pColor.alpha());
	}
}
