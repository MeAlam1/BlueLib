// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public abstract class JSONParser {

    protected Map<String, JsonObject> dataMap = new HashMap<>();

    protected static final JSONLoader jsonLoader = new JSONLoader();

    protected static final JSONMerger jsonMerger = new JSONMerger();

    protected JsonObject mergedJsonObject;

    public void loadData(String pFolderPath, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();
        mergedJsonObject = new JsonObject();

        Collection<ResourceLocation> resources = resourceManager.listResources(pFolderPath, path -> path.getPath().endsWith(".json")).keySet();

        BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.log("json.found", pFolderPath), true);

        for (ResourceLocation resourceLocation : resources) {
            try {
                JsonObject jsonObject = jsonLoader.loadJson(resourceLocation, resourceManager);
                jsonMerger.mergeJsonObjects(mergedJsonObject, jsonObject);
            } catch (Exception pException) {
                BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("json.failed", resourceLocation.toString()), pException, true);
            }
        }
    }

    public Map<String, JsonObject> getDataMap() {
        return dataMap;
    }

    public JsonObject getMergedJsonObject() {
        return mergedJsonObject;
    }
}
