/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.api.utils.variant.ParameterUtils;
import software.bluelib.json.JSONParser;

public class VariantLoader extends JSONParser {

	public static final Map<String, JsonObject> AllVariants = new HashMap<>();

	private static final VariantLoader LOADER = new VariantLoader();

	public static void loadVariants(String pFolderPath, ResourceManager pResourceManager, String pEntityName) {
		LOADER.loadData(pFolderPath, pResourceManager);
		AllVariants.putAll(LOADER.getDataMap());
		parseVariants(pEntityName, LOADER.getMergedJsonObject());
	}

	private static void parseVariants(String entityName, JsonObject variantsJson) {
		if (BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPre(entityName)) {
			BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.load.cancelled"), true);
			return;
		}

		if (!AllVariants.containsKey(entityName)) {
			for (String variantKey : variantsJson.keySet()) {
				if (BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPre(variantKey, entityName)) {
					BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variant.load.cancelled", variantKey, entityName), true);
					return;
				}
				BlueLibConstants.PlatformHelper.EVENT_PROXY.variantLoadedPost(entityName, variantKey);
			}
			AllVariants.put(entityName, variantsJson);
		}

		BlueLibConstants.PlatformHelper.EVENT_PROXY.allVariantsLoadedPost(entityName);

		BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.entities", Arrays.toString(ParameterUtils.getAllEntities().toArray())), true);
		BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.variants", entityName, Arrays.toString(Objects.requireNonNull(ParameterUtils.getVariantsOfEntity(entityName)).toArray())), true);
	}
}
