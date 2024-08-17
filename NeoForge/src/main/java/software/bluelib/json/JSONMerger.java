// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * A {@code Class} that merges JSON data from a source {@link JsonObject} into a target {@link JsonObject}.
 */
public class JSONMerger {

    /**
     * A {@code Void} that merges data from a source {@link JsonObject} into a target {@link JsonObject}.
     *
     * @param pTarget {@link JsonObject} - The target {@link JsonObject} to merge data into.
     * @param pSource {@link JsonObject} - The source {@link JsonObject} to merge data from.
     */
    public void mergeJsonObjects(JsonObject pTarget, JsonObject pSource) {
        for (Map.Entry<String, JsonElement> entry : pSource.entrySet()) {
            if (pTarget.has(entry.getKey())) {
                JsonArray targetArray = pTarget.getAsJsonArray(entry.getKey());
                JsonArray sourceArray = entry.getValue().getAsJsonArray();
                for (JsonElement element : sourceArray) {
                    targetArray.add(element);
                }
            } else {
                pTarget.add(entry.getKey(), entry.getValue());
            }
        }
    }
}
