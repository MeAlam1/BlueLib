/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

public class JSONMerger {

	public static void mergeJsonObjects(@NotNull JsonObject pTarget, @NotNull JsonObject pSource) {
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

					BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("json.merge", key));
				} else {
					pTarget.add(key, sourceElement);
					BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("json.overwrite", key));
				}
			} else {
				pTarget.add(key, sourceElement);
				BaseLogger.log(true, BaseLogLevel.SUCCESS, BlueTranslation.log("json.add", key));
			}
		}
	}
}
