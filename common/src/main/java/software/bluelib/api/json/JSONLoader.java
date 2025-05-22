/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class JSONLoader {

    private static final Gson gson = new Gson();

    public JsonObject loadJson(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
        try {
            Optional<Resource> resource = pResourceManager.getResource(pResourceLocation);

            if (resource.isEmpty()) {
                BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("json.notfound", pResourceLocation.toString()), true);
                return new JsonObject();
            }

            try (InputStream inputStream = resource.get().open();
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.log("json.loaded", pResourceLocation.toString()), true);
                return jsonObject;
            }
        } catch (IOException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("json.failed", pResourceLocation.toString()), pException, true);
            return new JsonObject();
        }
    }
}
