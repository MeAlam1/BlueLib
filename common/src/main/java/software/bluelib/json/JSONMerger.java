// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} responsible for merging JSON data from a source {@link JsonObject} into a target {@link JsonObject}.
 * <p>
 * This class provides functionality to combine JSON data where overlapping keys result in merging arrays,
 * and non-overlapping keys are simply added to the target.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #mergeJsonObjects(JsonObject, JsonObject)} - Merges the data from the source JSON object into the target JSON object.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class JSONMerger {

    /**
     * A {@code public void} method that merges data from a source {@link JsonObject} into a target {@link JsonObject}.
     * <p>
     * If the target JSON object already contains a key present in the source JSON object, the values are merged if they are arrays.
     * Otherwise, the source value is added to the target JSON object.
     * </p>
     *
     * @param pTarget {@link JsonObject} - The target {@link JsonObject} to merge data into. This object will be modified by adding or updating its values.
     * @param pSource {@link JsonObject} - The source {@link JsonObject} to merge data from. This object is not modified by the operation.
     * @author MeAlam
     * @since 1.0.0
     */
    public void mergeJsonObjects(JsonObject pTarget, JsonObject pSource) {
        for (Map.Entry<String, JsonElement> entry : pSource.entrySet()) {
            String key = entry.getKey();
            JsonElement sourceElement = entry.getValue();

            if (pTarget.has(key)) {
                JsonElement targetElement = pTarget.get(key);

                if (targetElement.isJsonArray() && sourceElement.isJsonArray()) {
                    JsonArray targetArray = targetElement.getAsJsonArray();
                    JsonArray sourceArray = sourceElement.getAsJsonArray();

                    for (JsonElement element : sourceArray) {
                        targetArray.add(element);
                    }

                    BaseLogger.log(BaseLogLevel.INFO, "Merged array for key: " + key, true);
                } else {
                    pTarget.add(key, sourceElement);
                    BaseLogger.log(BaseLogLevel.WARNING, "Overwriting value for key: " + key, true);
                }
            } else {
                pTarget.add(key, sourceElement);
                BaseLogger.log(BaseLogLevel.SUCCESS, "Added new key: " + key, true);
            }
        }
    }
}
