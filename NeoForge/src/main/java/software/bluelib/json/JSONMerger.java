// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * A {@code Class} responsible for merging JSON data from a source {@link JsonObject} into a target {@link JsonObject}.
 * <p>
 * This class provides functionality to combine JSON data where overlapping keys result in merging arrays,
 * and non-overlapping keys are simply added to the target.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #mergeJsonObjects(JsonObject, JsonObject)} - Merges the data from the source JSON object into the target JSON object.</li>
 * </ul>
 * </p>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class JSONMerger {

    /**
     * Merges data from a source {@link JsonObject} into a target {@link JsonObject}.
     * <p>
     * If the target JSON object already contains a key present in the source JSON object, the values are merged if they are arrays.
     * Otherwise, the source value is added to the target JSON object.
     * </p>
     *
     * @param pTarget {@link JsonObject} - The target {@link JsonObject} to merge data into. This object will be modified by adding or updating its values.
     * @param pSource {@link JsonObject} - The source {@link JsonObject} to merge data from. This object is not modified by the operation.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public void mergeJsonObjects(JsonObject pTarget, JsonObject pSource) {
        for (Map.Entry<String, JsonElement> entry : pSource.entrySet()) {
            if (pTarget.has(entry.getKey())) {
                JsonElement targetElement = pTarget.get(entry.getKey());
                JsonElement sourceElement = entry.getValue();

                if (targetElement.isJsonArray() && sourceElement.isJsonArray()) {
                    JsonArray targetArray = targetElement.getAsJsonArray();
                    JsonArray sourceArray = sourceElement.getAsJsonArray();

                    for (JsonElement element : sourceArray) {
                        targetArray.add(element);
                    }
                } else {
                    pTarget.add(entry.getKey(), sourceElement);
                }
            } else {
                pTarget.add(entry.getKey(), entry.getValue());
            }
        }
    }
}
