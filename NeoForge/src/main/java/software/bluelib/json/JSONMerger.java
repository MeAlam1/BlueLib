// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * General {@link JSONMerger}  to merge JSON data.
 */
public class JSONMerger {

    /**
     * Merges data from a source {@link JsonObject} into a target {@link JsonObject}.
     *
     * @param pTarget  The target {@link JsonObject} to merge data into.
     * @param pSource  The source {@link JsonObject} to merge data from.
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
