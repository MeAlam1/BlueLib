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
import software.bluelib.api.json.deserializer.color.RGBColor;

public record RGBColorCache(
		@NotNull Integer red,
		@NotNull Integer green,
		@NotNull Integer blue) {

	@NotNull
	public static final Codec<RGBColorCache> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
				return DataResult.success(RGBColorCache.readFromNBT(tag));
			},
			colorCache -> {
				CompoundTag tag = new CompoundTag();
				colorCache.writeToNBT(tag);
				return new Dynamic<>(NbtOps.INSTANCE, tag);
			});

	@NotNull
	public static final DataComponentType<RGBColorCache> RGB_COLOR_DATA = DataComponentType.<RGBColorCache>builder()
			.persistent(CODEC)
			.build();

	public void writeToNBT(@NotNull CompoundTag pTag) {
		pTag.putInt("Red", red);
		pTag.putInt("Green", green);
		pTag.putInt("Blue", blue);
	}

	@NotNull
	public static RGBColorCache readFromNBT(@NotNull CompoundTag pTag) {
		return new RGBColorCache(pTag.getInt("Red"), pTag.getInt("Green"), pTag.getInt("Blue"));
	}

	@NotNull
	public static RGBColorCache construct(@NotNull RGBColor pColor) {
		return new RGBColorCache(pColor.red(), pColor.green(), pColor.blue());
	}

	public static Color convert(@NotNull RGBColor pColor) {
		return new Color(pColor.red(), pColor.green(), pColor.blue());
	}

	public static Color convert(@NotNull RGBColorCache pColor) {
		return new Color(pColor.red(), pColor.green(), pColor.blue());
	}
}
