// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.interfaces.variant.base.IVariantEntityBase;
import software.bluelib.json.JSONLoader;
import software.bluelib.json.JSONMerger;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.variant.ParameterUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code public class} that implements the {@link IVariantEntityBase} {@code interface} that manages the loading and storage of entity variants.
 * <p>
 * The class handles loading and merging of JSON Data by utilizing the {@link JSONLoader} and {@link JSONMerger} classes. <br>
 * To load the Variants it loops through all resources in a folder and merges them into a single {@link JsonObject}. <br>
 * The merged JSON data is then parsed into {} instances and stored in {@link #AllVariants}. <br>
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #loadVariants(String, MinecraftServer, String)} - Loads and merges variant data by looping thru all resources in a folder.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @since 1.0.0
 */
public class VariantLoader implements IVariantEntityBase {

    /**
     * A {@code public static} {@link Map} that stores all variants of an entity.
     * <p>
     * The field is used to store all variants of an entity and is accessed by the {@link ParameterUtils} class.
     * </p>
     * <br>
     * <strong>We don't recommend using this field directly unless you know what you are doing.</strong>
     *
     * @since 1.3.0
     */
    public static Map<String, JsonObject> AllVariants = new HashMap<>();

    /**
     * A {@code private static final} {@link JSONLoader} to load JSON data from resources.
     *
     * @since 1.0.0
     */
    private static final JSONLoader jsonLoader = new JSONLoader();

    /**
     * A {@code private static final} {@link JSONMerger} to merge JSON data.
     * <p>
     * This {@link JSONMerger} instance is used to merge JSON data into a single {@link JsonObject}.
     * </p>
     *
     * @since 1.0.0
     */
    private static final JSONMerger jsonMerger = new JSONMerger();

    /**
     * A {@code public static void} that loads and merges variant data from JSON resources in the specified folder path.
     * <p>
     * The method loops through all resources in the folder and merges them into a single {@link JsonObject}. <br>
     * The merged JSON data is then parsed into {@link JsonObject} instances and stored in {@link #AllVariants}. <br>
     * </p>
     *
     * @param pFolderPath {@link String} - The path to the folder containing JSON resources.
     * @param pServer     {@link MinecraftServer} - The {@link MinecraftServer} instance used to access resources.
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared before loading new ones.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void loadVariants(String pFolderPath, MinecraftServer pServer, String pEntityName) {
        ResourceManager resourceManager = pServer.getResourceManager();
        JsonObject mergedJsonObject = new JsonObject();

        Collection<ResourceLocation> collection = resourceManager.listResources(pFolderPath, pFiles -> pFiles.getPath().endsWith(".json")).keySet();

        BaseLogger.log(BaseLogLevel.INFO, "Found resources: " + collection + " at: " + pFolderPath + " for: " + pEntityName, true);

        for (ResourceLocation resourceLocation : collection) {
            try {
                BaseLogger.log(BaseLogLevel.INFO, "Loading JSON data from resource: " + resourceLocation.toString(), true);
                JsonObject jsonObject = jsonLoader.loadJson(resourceLocation, resourceManager);
                jsonMerger.mergeJsonObjects(mergedJsonObject, jsonObject);
            } catch (Exception pException) {
                BaseLogger.log(BaseLogLevel.ERROR, "Failed to load JSON data from resource: " + resourceLocation.toString(), pException, true);
            }
        }
        parseVariants(pEntityName, mergedJsonObject);
    }

    /**
     * A {@code private static void} that parses the merged {@link JsonObject} containing variant data.
     * <p>
     * The method parses the merged {@link JsonObject} and stores the data in the {@link #AllVariants} map.
     * The map is used to store all variants of an entity.
     * </p>
     *
     * @param pJsonObject {@link JsonObject} - The merged {@link JsonObject} containing variant data.
     * @author MeAlam
     * @since 1.0.0
     */
    private static void parseVariants(String pEntityName, JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> ignored : pJsonObject.entrySet()) {
            AllVariants.putIfAbsent(pEntityName, pJsonObject);
        }
        BaseLogger.log(BaseLogLevel.INFO, "All Entities: " + ParameterUtils.getAllEntities(), true);
        BaseLogger.log(BaseLogLevel.INFO, "Variants of " + pEntityName + ": " + ParameterUtils.getVariantsOfEntity(pEntityName), true);
    }
}
