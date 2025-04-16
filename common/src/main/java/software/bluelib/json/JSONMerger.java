// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class JSONMerger {

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
