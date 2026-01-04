/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.loader;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({ "unused" })
public final class JsonUtils {

	private JsonUtils() {}

	@NotNull
	public static List<Float> jsonArrayToFloatList(@Nullable JsonArray pArray) throws JsonParseException {
		if (pArray == null)
			return new ArrayList<>();

		List<Float> output = new ArrayList<>(pArray.size());

		for (int i = 0; i < pArray.size(); i++) {
			output.add(pArray.get(i).getAsFloat());
		}

		return output;
	}

	@NotNull
	public static List<Double> jsonArrayToDoubleList(@Nullable JsonArray pArray) throws JsonParseException {
		if (pArray == null)
			return new ArrayList<>();

		List<Double> output = new ArrayList<>(pArray.size());

		for (int i = 0; i < pArray.size(); i++) {
			output.add(pArray.get(i).getAsDouble());
		}

		return output;
	}

	@NotNull
	public static List<Integer> jsonArrayToIntList(@Nullable JsonArray pArray) throws JsonParseException {
		if (pArray == null)
			return new ArrayList<>();

		List<Integer> output = new ArrayList<>(pArray.size());

		for (int i = 0; i < pArray.size(); i++) {
			output.add(pArray.get(i).getAsInt());
		}

		return output;
	}

	@NotNull
	public static <T> List<T> jsonArrayToObjectList(
			@Nullable JsonArray pArray,
			@NotNull JsonDeserializationContext pContext,
			@NotNull Class<? extends T> pObjectClass) {
		if (pArray == null)
			return new ArrayList<>();

		List<T> list = new ArrayList<>(pArray.size());

		for (int i = 0; i < pArray.size(); i++) {
			list.add(pContext.deserialize(pArray.get(i), pObjectClass));
		}

		return list;
	}

	@NotNull
	public static <T> List<T> jsonArrayToList(@Nullable JsonArray pArray, @NotNull Function<JsonElement, T> pElementTransformer) {
		if (pArray == null)
			return new ObjectArrayList<>();

		List<T> list = new ObjectArrayList<>(pArray.size());

		for (JsonElement element : pArray) {
			list.add(pElementTransformer.apply(element));
		}

		return list;
	}

	@NotNull
	public static List<String> jsonArrayToStringList(@Nullable JsonArray pArray) throws JsonParseException {
		if (pArray == null)
			return new ArrayList<>();

		List<String> output = new ArrayList<>(pArray.size());

		for (int i = 0; i < pArray.size(); i++) {
			output.add(pArray.get(i).getAsString());
		}

		return output;
	}

	@NotNull
	public static <T> Map<String, T> jsonObjToMap(@NotNull JsonObject pObj, @NotNull JsonDeserializationContext pContext, @NotNull Class<T> pObjectType) {
		Map<String, T> map = new Object2ObjectOpenHashMap<>(pObj.size());

		for (Map.Entry<String, JsonElement> entry : pObj.entrySet()) {
			map.put(entry.getKey(), pContext.deserialize(entry.getValue(), pObjectType));
		}

		return map;
	}

	@NotNull
	public static <T> Map<String, List<T>> jsonObjToListMap(@NotNull JsonObject pObj, @NotNull JsonDeserializationContext pContext, @NotNull Class<T> pObjectType) {
		Map<String, List<T>> map = new Object2ObjectOpenHashMap<>(pObj.size());
		for (Map.Entry<String, JsonElement> entry : pObj.entrySet()) {
			JsonArray arr = entry.getValue().getAsJsonArray();
			List<T> list = JsonUtils.jsonArrayToObjectList(arr, pContext, pObjectType);
			map.put(entry.getKey(), list);
		}
		return map;
	}

	@NotNull
	public static JsonObject filterJsonObject(@NotNull JsonObject pSource, @NotNull String... pAvoid) {
		JsonObject result = new JsonObject();
		for (Map.Entry<String, JsonElement> entry : pSource.entrySet()) {
			String key = entry.getKey();
			boolean skip = false;
			for (String avoid : pAvoid) {
				if (key.equals(avoid)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				result.add(key, entry.getValue());
			}
		}
		return result;
	}

	@NotNull
	public static <T> JsonDeserializer<T> unionDeserializer(
			@NotNull BiFunction<JsonArray, JsonDeserializationContext, T> pArrayMapper,
			@NotNull BiFunction<JsonObject, JsonDeserializationContext, T> pObjectMapper) {
		return (json, type, context) -> {
			if (json.isJsonArray()) {
				return pArrayMapper.apply(json.getAsJsonArray(), context);
			} else if (json.isJsonObject()) {
				return pObjectMapper.apply(json.getAsJsonObject(), context);
			} else {
				throw new JsonParseException("Expected JSON array or object but got: " + json);
			}
		};
	}

	private static boolean hasNonNull(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return pObj.has(pElementName) && !pObj.get(pElementName).isJsonNull();
	}
	
	@Nullable
	public static JsonPrimitive getOptionalPrimitive(@NotNull JsonObject pObj, @NotNull String pElementName) {
		return hasNonNull(pObj, pElementName) ? pObj.getAsJsonPrimitive(pElementName) : null;
	}

	@Nullable
	public static Long getOptionalLong(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsLong(pObj, pElementName) : null;
	}

	@Nullable
	public static Boolean getOptionalBoolean(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsBoolean(pObj, pElementName) : null;
	}

	@Nullable
	public static Float getOptionalFloat(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsFloat(pObj, pElementName) : null;
	}

	@Nullable
	public static Double getOptionalDouble(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsDouble(pObj, pElementName) : null;
	}

	@Nullable
	public static Integer getOptionalInteger(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsInt(pObj, pElementName) : null;
	}

	@Nullable
	public static String getOptionalString(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsString(pObj, pElementName) : null;
	}

	@Nullable
	public static <T> T getOptionalObject(@NotNull JsonObject pObj, @Nullable String pElementName, @NotNull JsonDeserializationContext pContext, @NotNull Class<T> pType) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsObject(pObj, pElementName, pContext, pType) : null;
	}

	@Nullable
	public static JsonArray getOptionalJsonArray(@NotNull JsonObject pObj, @Nullable String pElementName) {
		return hasNonNull(pObj, pElementName) ? GsonHelper.getAsJsonArray(pObj, pElementName) : null;
	}
}
