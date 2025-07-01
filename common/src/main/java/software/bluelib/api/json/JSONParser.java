/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

public abstract class JSONParser {

    protected Map<String, JsonObject> dataMap = new HashMap<>();

    protected static final Gson gson = new Gson();

    protected static final JSONMerger jsonMerger = new JSONMerger();

    protected JsonObject mergedJsonObject;

    public void loadData(@NotNull String pFolderPath, @NotNull ResourceManager pResourceManager) {
        mergedJsonObject = new JsonObject();

        Collection<ResourceLocation> resources = pResourceManager.listResources(pFolderPath, path -> path.getPath().endsWith(".json")).keySet();

        BaseLogger.log(true, BaseLogLevel.SUCCESS, BlueTranslation.log("json.found", pFolderPath));

        for (ResourceLocation resourceLocation : resources) {
            try {
                Optional<Resource> optionalResource = pResourceManager.getResource(resourceLocation);
                if (optionalResource.isPresent()) {
                    Resource resource = optionalResource.get();
                    try (InputStream inputStream = resource.open();
                            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                        JsonElement element = GsonHelper.fromJson(gson, reader, JsonElement.class);
                        if (element.isJsonObject()) {
                            JsonObject jsonObject = element.getAsJsonObject();
                            jsonMerger.mergeJsonObjects(mergedJsonObject, jsonObject);
                        }
                    }
                }
            } catch (Exception pException) {
                BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("json.failed", resourceLocation.toString()), pException);
            }
        }
    }

    @NotNull
    public Map<String, JsonObject> getDataMap() {
        return dataMap;
    }

    @NotNull
    public JsonObject getMergedJsonObject() {
        return mergedJsonObject;
    }
}
