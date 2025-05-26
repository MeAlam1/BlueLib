/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.util;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class JsonUtil {

    private JsonUtil() {}

    public static double[] jsonArrayToDoubleArray(@Nullable JsonArray pArray) throws JsonParseException {
        if (pArray == null)
            return new double[3];

        double[] output = new double[pArray.size()];

        for (int i = 0; i < pArray.size(); i++) {
            output[i] = pArray.get(i).getAsDouble();
        }

        return output;
    }

    public static <T> T[] jsonArrayToObjectArray(JsonArray pArray, JsonDeserializationContext pContext, Class<T> pObjectClass) {
        T[] objArray = (T[]) Array.newInstance(pObjectClass, pArray.size());

        for (int i = 0; i < pArray.size(); i++) {
            objArray[i] = pContext.deserialize(pArray.get(i), pObjectClass);
        }

        return objArray;
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

    public static <T> Map<String, T> jsonObjToMap(JsonObject obj, JsonDeserializationContext pContext, Class<T> pObjectType) {
        Map<String, T> map = new Object2ObjectOpenHashMap<>(obj.size());

        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            map.put(entry.getKey(), pContext.deserialize(entry.getValue(), pObjectType));
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
}
