/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.variants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.codec.NbtCodecUtils;
import software.bluelib.api.utils.minecraft.CompoundTagUtils;

public record VariantCache(
		@Nullable JsonArray parameters) {

	@NotNull
	public static final Codec<VariantCache> CODEC = NbtCodecUtils.fromNbt(VariantCache::readFromNBT, VariantCache::writeToNBT);

	@NotNull
	public static final DataComponentType<VariantCache> VARIANT_CACHE_DATA = NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		CompoundTagUtils.writeJsonArray(pTag, "parameters", parameters);
	}

	@NotNull
	public static VariantCache readFromNBT(@NotNull CompoundTag pTag) {
		return new VariantCache(CompoundTagUtils.readJsonArray(pTag, "parameters"));
	}

	@Nullable
	public JsonElement getParameter(@NotNull String pParameterName) {
		if (parameters == null) return null;
		for (JsonElement element : parameters) {
			if (element.isJsonObject() && element.getAsJsonObject().has(pParameterName)) {
				return element.getAsJsonObject().get(pParameterName);
			}
		}
		return null;
	}
}
