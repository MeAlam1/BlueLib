package software.bluelib.json;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class JSONParser {
    protected Map<String, JsonObject> dataMap = new HashMap<>();
    protected static final JSONLoader jsonLoader = new JSONLoader();
    protected static final JSONMerger jsonMerger = new JSONMerger();
    protected JsonObject mergedJsonObject;

    public void loadData(String pFolderPath, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();
        mergedJsonObject = new JsonObject();

        Collection<ResourceLocation> resources =
                resourceManager.listResources(pFolderPath, path -> path.getPath().endsWith(".json")).keySet();

        BaseLogger.log(BaseLogLevel.INFO, "Found resources: " + resources + " at: " + pFolderPath);

        for (ResourceLocation resourceLocation : resources) {
            try {
                BaseLogger.log(BaseLogLevel.INFO, "Loading JSON data from resource: " + resourceLocation);
                JsonObject jsonObject = jsonLoader.loadJson(resourceLocation, resourceManager);
                jsonMerger.mergeJsonObjects(mergedJsonObject, jsonObject);
            } catch (Exception exception) {
                BaseLogger.log(BaseLogLevel.ERROR, "Failed to load JSON data from resource: " + resourceLocation, exception);
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
