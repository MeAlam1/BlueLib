/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({ "unused" })
public final class JsonUtils {

    private JsonUtils() {}

    public static List<Float> jsonArrayToFloatList(@Nullable JsonArray pArray) throws JsonParseException {
        if (pArray == null)
            return new ArrayList<>();

        List<Float> output = new ArrayList<>(pArray.size());

        for (int i = 0; i < pArray.size(); i++) {
            output.add(pArray.get(i).getAsFloat());
        }

        return output;
    }

    public static List<Double> jsonArrayToDoubleList(@Nullable JsonArray pArray) throws JsonParseException {
        if (pArray == null)
            return new ArrayList<>();

        List<Double> output = new ArrayList<>(pArray.size());

        for (int i = 0; i < pArray.size(); i++) {
            output.add(pArray.get(i).getAsDouble());
        }

        return output;
    }

    public static List<Integer> jsonArrayToIntList(@Nullable JsonArray pArray) throws JsonParseException {
        if (pArray == null)
            return new ArrayList<>();

        List<Integer> output = new ArrayList<>(pArray.size());

        for (int i = 0; i < pArray.size(); i++) {
            output.add(pArray.get(i).getAsInt());
        }

        return output;
    }

    public static <T> List<T> jsonArrayToObjectList(JsonArray pArray, JsonDeserializationContext pContext, Class<T> pObjectClass) {
        if (pArray == null)
            return new ArrayList<>();

        List<T> list = new ArrayList<>(pArray.size());

        for (int i = 0; i < pArray.size(); i++) {
            list.add(pContext.deserialize(pArray.get(i), pObjectClass));
        }

        return list;
    }

    public static <T> List<T> jsonArrayToList(@Nullable JsonArray pArray, Function<JsonElement, T> pElementTransformer) {
        if (pArray == null)
            return new ObjectArrayList<>();

        List<T> list = new ObjectArrayList<>(pArray.size());

        for (JsonElement element : pArray) {
            list.add(pElementTransformer.apply(element));
        }

        return list;
    }

    public static List<String> jsonArrayToStringList(@Nullable JsonArray pArray) throws JsonParseException {
        if (pArray == null)
            return new ArrayList<>();

        List<String> output = new ArrayList<>(pArray.size());

        for (int i = 0; i < pArray.size(); i++) {
            output.add(pArray.get(i).getAsString());
        }

        return output;
    }

    public static <T> Map<String, T> jsonObjToMap(JsonObject pObj, JsonDeserializationContext pContext, Class<T> pObjectType) {
        Map<String, T> map = new Object2ObjectOpenHashMap<>(pObj.size());

        for (Map.Entry<String, JsonElement> entry : pObj.entrySet()) {
            map.put(entry.getKey(), pContext.deserialize(entry.getValue(), pObjectType));
        }

        return map;
    }

    public static <T> Map<String, List<T>> jsonObjToListMap(JsonObject pObj, JsonDeserializationContext pContext, Class<T> pObjectType) {
        Map<String, List<T>> map = new Object2ObjectOpenHashMap<>(pObj.size());
        for (Map.Entry<String, JsonElement> entry : pObj.entrySet()) {
            JsonArray arr = entry.getValue().getAsJsonArray();
            List<T> list = JsonUtils.jsonArrayToObjectList(arr, pContext, pObjectType);
            map.put(entry.getKey(), list);
        }
        return map;
    }

    @Nullable
    public static Long getOptionalLong(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsLong(pObj, pElementName) : null;
    }

    @Nullable
    public static Boolean getOptionalBoolean(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsBoolean(pObj, pElementName) : null;
    }

    @Nullable
    public static Float getOptionalFloat(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsFloat(pObj, pElementName) : null;
    }

    @Nullable
    public static Double getOptionalDouble(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsDouble(pObj, pElementName) : null;
    }

    @Nullable
    public static Integer getOptionalInteger(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsInt(pObj, pElementName) : null;
    }

    @Nullable
    public static String getOptionalString(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsString(pObj, pElementName) : null;
    }

    @Nullable
    public static <T> T getOptionalObject(JsonObject pObj, String pElementName, JsonDeserializationContext pContext, Class<T> pType) {
        return pObj.has(pElementName) ? GsonHelper.getAsObject(pObj, pElementName, pContext, pType) : null;
    }

    @Nullable
    public static JsonArray getOptionalJsonArray(JsonObject pObj, String pElementName) {
        return pObj.has(pElementName) ? GsonHelper.getAsJsonArray(pObj, pElementName) : null;
    }
}
