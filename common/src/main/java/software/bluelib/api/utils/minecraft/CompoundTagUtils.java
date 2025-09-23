/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompoundTagUtils {

	public static <K, V> void writeMap(
			@NotNull CompoundTag pTag,
			@NotNull String pKey,
			@NotNull Map<K, V> pMap,
			@NotNull BiConsumer<CompoundTag, K> pKeyWriter,
			@NotNull BiConsumer<CompoundTag, V> pValueWriter,
			@NotNull String pKeyFieldName,
			@NotNull String pValueFieldName) {
		ListTag list = new ListTag();
		for (Map.Entry<K, V> entry : pMap.entrySet()) {
			CompoundTag pairTag = new CompoundTag();
			CompoundTag keyTag = new CompoundTag();
			CompoundTag valueTag = new CompoundTag();
			pKeyWriter.accept(keyTag, entry.getKey());
			pValueWriter.accept(valueTag, entry.getValue());
			pairTag.put(pKeyFieldName, keyTag);
			pairTag.put(pValueFieldName, valueTag);
			list.add(pairTag);
		}
		pTag.put(pKey, list);
	}

	@NotNull
	public static <K, V> Map<K, V> readMap(
			@NotNull CompoundTag pTag,
			@NotNull String pKey,
			@NotNull Function<CompoundTag, K> pKeyReader,
			@NotNull Function<CompoundTag, V> pValueReader,
			@NotNull String pKeyFieldName,
			@NotNull String pValueFieldName) {
		Map<K, V> map = new HashMap<>();
		ListTag list = pTag.getList(pKey, CompoundTag.TAG_COMPOUND);
		for (Tag element : list) {
			if (element instanceof CompoundTag pairTag) {
				CompoundTag keyTag = pairTag.getCompound(pKeyFieldName);
				CompoundTag valueTag = pairTag.getCompound(pValueFieldName);
				K key = pKeyReader.apply(keyTag);
				V value = pValueReader.apply(valueTag);
				map.put(key, value);
			}
		}
		return map;
	}

	public static void writeJsonArray(@NotNull CompoundTag pTag, @NotNull String pKey, @Nullable JsonArray pArray) {
		if (pArray == null) pArray = new JsonArray();
		pTag.putString(pKey, pArray.toString());
	}

	@NotNull
	public static JsonArray readJsonArray(@NotNull CompoundTag pTag, @NotNull String pKey) {
		String json = pTag.getString(pKey);
		return JsonParser.parseString(json).getAsJsonArray();
	}

	public static <T> void writeList(
			@NotNull CompoundTag pTag,
			@NotNull String pKey,
			@NotNull List<T> pList,
			@NotNull BiConsumer<CompoundTag, T> pElementWriter) {
		ListTag listTag = new ListTag();
		for (T element : pList) {
			CompoundTag elementTag = new CompoundTag();
			pElementWriter.accept(elementTag, element);
			listTag.add(elementTag);
		}
		pTag.put(pKey, listTag);
	}

	@NotNull
	public static <T> List<T> readList(
			@NotNull CompoundTag pTag,
			@NotNull String pKey,
			@NotNull Function<CompoundTag, T> pElementReader) {
		ListTag listTag = pTag.getList(pKey, CompoundTag.TAG_COMPOUND);
		List<T> result = new ArrayList<>();
		for (Tag tag : listTag) {
			if (tag instanceof CompoundTag elementTag) {
				result.add(pElementReader.apply(elementTag));
			}
		}
		return result;
	}
}
