/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

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

                    BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("json.merge", key), true);
                } else {
                    pTarget.add(key, sourceElement);
                    BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("json.overwrite", key), true);
                }
            } else {
                pTarget.add(key, sourceElement);
                BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.log("json.add", key), true);
            }
        }
    }
}
