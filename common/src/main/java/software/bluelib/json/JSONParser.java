// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * An abstract class for parsing JSON data.
 * <p>
 * Purpose: This class provides methods to load and merge JSON data from specified folder paths.<br>
 * When: The JSON data is loaded and merged when the {@link #loadData(String, MinecraftServer)} method is called.<br>
 * Where: The JSON data is loaded from the specified folder path and merged into a single {@link JsonObject}.<br>
 * Additional Info: This class uses {@link JSONLoader} to load JSON data and {@link JSONMerger} to merge JSON objects.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #loadData(String, MinecraftServer)} - Loads and merges JSON data from the specified folder path.</li>
 * <li>{@link #getDataMap()} - Returns the data map containing JSON data.</li>
 * <li>{@link #getMergedJsonObject()} - Returns the merged JSON object.</li>
 * </ul>
 *
 * @see JSONLoader
 * @see JSONMerger
 * @see MinecraftServer
 * @see JsonObject
 * @see ResourceManager
 * @see ResourceLocation
 * @see Collection
 * @since 1.7.0
 * @version 1.7.0
 * @author MeAlam
 */
public abstract class JSONParser {

    /**
     * A map to store JSON data with their corresponding keys.
     * <p>
     * Purpose: This map holds the JSON data loaded from the specified folder path.<br>
     * When: The map is populated when the {@link #loadData(String, MinecraftServer)} method is called.<br>
     * Where: The map is used to store JSON data with their corresponding keys.<br>
     * Additional Info: The keys are the resource locations of the JSON files.
     * </p>
     *
     * @since 1.7.0
     */
    protected Map<String, JsonObject> dataMap = new HashMap<>();

    /**
     * A static instance of JSONLoader to load JSON data.
     * <p>
     * Purpose: This instance is used to load JSON data from the specified folder path.<br>
     * When: The instance is used when the {@link #loadData(String, MinecraftServer)} method is called.<br>
     * Where: The instance is used to load JSON data from the specified folder path.<br>
     * Additional Info: The JSONLoader class provides methods to load JSON data from resource locations.
     * </p>
     *
     * @see JSONLoader
     * @since 1.7.0
     */
    protected static final JSONLoader jsonLoader = new JSONLoader();

    /**
     * A static instance of JSONMerger to merge JSON objects.
     * <p>
     * Purpose: This instance is used to merge JSON objects loaded from the specified folder path.<br>
     * When: The instance is used when the {@link #loadData(String, MinecraftServer)} method is called.<br>
     * Where: The instance is used to merge JSON objects loaded from the specified folder path.<br>
     * Additional Info: The JSONMerger class provides methods to merge JSON objects.
     * </p>
     *
     * @see JSONMerger
     * @since 1.7.0
     */
    protected static final JSONMerger jsonMerger = new JSONMerger();

    /**
     * A JsonObject to store the merged JSON data.
     * <p>
     * Purpose: This object holds the merged JSON data from the specified folder path.<br>
     * When: The object is populated when the {@link #loadData(String, MinecraftServer)} method is called.<br>
     * Where: The object is used to store the merged JSON data.<br>
     * Additional Info: The merged JSON data is a combination of all JSON files loaded from the specified folder path.
     * </p>
     *
     * @since 1.7.0
     */
    protected JsonObject mergedJsonObject;

    /**
     * Loads JSON data from the specified folder path and merges them.
     * <p>
     * Purpose: This method loads JSON data from the specified folder path and merges them into a single {@link JsonObject}.<br>
     * When: The method is called to load and merge JSON data.<br>
     * Where: The method is called whenever JSON data needs to be loaded and merged.<br>
     * Additional Info: The method uses {@link JSONLoader} to load JSON data and {@link JSONMerger} to merge JSON objects.
     * </p>
     *
     * @param pFolderPath The folder path to load JSON data from.
     * @param pServer     The Minecraft server instance.
     * @since 1.7.0
     * @author MeAlam
     * @see JSONLoader
     * @see JSONMerger
     * @see MinecraftServer
     * @see JsonObject
     * @see ResourceManager
     * @see ResourceLocation
     * @see Collection
     */
    public void loadData(String pFolderPath, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();
        mergedJsonObject = new JsonObject();

        Collection<ResourceLocation> resources = resourceManager.listResources(pFolderPath, path -> path.getPath().endsWith(".json")).keySet();

        BaseLogger.log(BaseLogLevel.INFO, "Found resources: " + resources + " at: " + pFolderPath, true);

        for (ResourceLocation resourceLocation : resources) {
            try {
                BaseLogger.log(BaseLogLevel.INFO, "Loading JSON data from resource: " + resourceLocation, true);
                JsonObject jsonObject = jsonLoader.loadJson(resourceLocation, resourceManager);
                jsonMerger.mergeJsonObjects(mergedJsonObject, jsonObject);
            } catch (Exception exception) {
                BaseLogger.log(BaseLogLevel.ERROR, "Failed to load JSON data from resource: " + resourceLocation, exception, true);
            }
        }
    }

    /**
     * Returns the data map containing JSON data.
     * <p>
     * Purpose: This method returns the data map containing JSON data loaded from the specified folder path.<br>
     * When: The method is called to retrieve the data map.<br>
     * Where: The method is called from any class or method.<br>
     * Additional Info: The data map contains JSON data with their corresponding keys.
     * </p>
     *
     * @return The data map.
     * @since 1.7.0
     * @author MeAlam
     * @see JsonObject
     * @see Map
     */
    public Map<String, JsonObject> getDataMap() {
        return dataMap;
    }

    /**
     * Returns the merged JSON object.
     * <p>
     * Purpose: This method returns the merged JSON object containing data from all JSON files loaded from the specified folder path.<br>
     * When: The method is called to retrieve the merged JSON object.<br>
     * Where: The method is called from any class or method.<br>
     * Additional Info: The merged JSON object is a combination of all JSON files loaded from the specified folder path.
     * </p>
     *
     * @return The merged JSON object.
     * @since 1.7.0
     * @author MeAlam
     * @see JsonObject
     */
    public JsonObject getMergedJsonObject() {
        return mergedJsonObject;
    }
}
