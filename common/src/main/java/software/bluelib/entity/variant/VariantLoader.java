// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonArray;
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

import java.util.*;

/**
 * A {@code public class} that implements the {@link IVariantEntityBase} {@code interface} that manages the loading and storage of entity variants.
 * <p>
 * The class handles loading and merging of JSON Data by utilizing the {@link JSONLoader} and {@link JSONMerger} classes. <br>
 * To load the Variants it loops through all resources in a folder and merges them into a single {@link JsonObject}. <br>
 * The merged JSON data is then parsed into {@link VariantParameter} instances and stored in {@link #entityVariantsMap}. <br>
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #loadVariants(String, MinecraftServer, String)} - Loads and merges variant data by looping thru all resources in a folder.</li>
 *   <li>{@link #getVariantsFromEntity(String)} - Retrieves the list of loaded {@link VariantParameter} for a specific entity.</li>
 *   <li>{@link #getVariantByName(String, String)} - Retrieves a specific {@link VariantParameter} by its name for a given entity.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class VariantLoader implements IVariantEntityBase {

    /**
     * A {@code private static final} {@link Map<String>} to store entity variants as key-value pairs.
     * <p>
     * This {@link Map<String>} holds entity names and their corresponding list of {@link VariantParameter} instances.
     * </p>
     *
     * @since 1.0.0
     */
    private static final Map<String, List<VariantParameter>> entityVariantsMap = new HashMap<>();

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
     * The merged JSON data is then parsed into {@link VariantParameter} instances and stored in {@link #entityVariantsMap}.
     * </p>
     *
     * @param pFolderPath {@link String} - The path to the folder containing JSON resources.
     * @param pServer     {@link MinecraftServer} - The {@link MinecraftServer} instance used to access resources.
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared before loading new ones.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void loadVariants(String pFolderPath, MinecraftServer pServer, String pEntityName) {

        clearVariantsForEntity(pEntityName);

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
        parseVariants(mergedJsonObject);
    }

    /**
     * A {@code private static void} that clears variants for a specific entity type from {@link #entityVariantsMap}.
     * <p>
     * This method removes all variants associated with the given entity name.
     * </p>
     *
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared.
     * @author MeAlam
     * @since 1.0.0
     */
    private static void clearVariantsForEntity(String pEntityName) {
        entityVariantsMap.remove(pEntityName);
    }

    /**
     * A {@code private static void} that parses the merged JSON data and converts it into {@link VariantParameter} instances.
     * <p>
     * This method processes each entry in the JSON object and stores the created {@link VariantParameter} instances in {@link #entityVariantsMap}.
     * </p>
     *
     * @param pJsonObject {@link JsonObject} - The merged {@link JsonObject} containing variant data.
     * @author MeAlam
     * @since 1.0.0
     */
    private static void parseVariants(JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            String entityName = entry.getKey();
            JsonArray textureArray = entry.getValue().getAsJsonArray();

            BaseLogger.log(BaseLogLevel.INFO, "Parsing variants for entity: " + entityName, true);
            List<VariantParameter> variantList = entityVariantsMap.computeIfAbsent(entityName, k -> new ArrayList<>());

            for (JsonElement variant : textureArray) {
                VariantParameter newVariant = getEntityVariant(entityName, variant.getAsJsonObject());

                boolean variantExists = variantList.stream()
                        .anyMatch(v -> v.equals(newVariant));

                if (!variantExists) {
                    variantList.add(newVariant);
                }
            }
        }
    }

    /**
     * A {@code private static} {@link VariantParameter} that creates a new {@link VariantParameter} instance from a JSON object.
     * <p>
     * This method wraps the creation of {@link VariantParameter} instances for easier management and potential modification.
     * </p>
     *
     * @param pJsonKey    {@link String} - The key associated with this variant.
     * @param pJsonObject {@link JsonObject} - The {@link JsonObject} containing the variant data.
     * @return {@link VariantParameter} - A {@link VariantParameter} instance.
     * @author MeAlam
     * @since 1.0.0
     */
    private static VariantParameter getEntityVariant(String pJsonKey, JsonObject pJsonObject) {
        return new VariantParameter(pJsonKey, pJsonObject);
    }

    /**
     * A {@code public static} {@link List<VariantParameter>} that retrieves the {@link List<VariantParameter>} of loaded {@link VariantParameter} instances for a specific entity.
     * <p>
     * This method returns a list of variants for the given entity name. If no variants are found, an empty list is returned.
     * </p>
     *
     * @param pEntityName {@link String} - The name of the entity to retrieve variants for.
     * @return {@link List<VariantParameter>} - A {@link List<VariantParameter>} of {@link VariantParameter} instances for the specified entity.
     * @author MeAlam
     * @since 1.0.0
     */
    public static List<VariantParameter> getVariantsFromEntity(String pEntityName) {
        BaseLogger.log(BaseLogLevel.INFO, "Retrieving variants for entity: " + pEntityName, true);
        return entityVariantsMap.getOrDefault(pEntityName, new ArrayList<>());
    }

    /**
     * A {@code public static} {@link VariantParameter} that retrieves a {@link VariantParameter} for a specific entity, by the variant's name.
     * <p>
     * This method searches for a variant with the specified name within the list of variants for the given entity.
     * </p>
     *
     * @param pEntityName  {@link String} - The name of the entity to retrieve variants for.
     * @param pVariantName {@link String} - The name of the variant to retrieve.
     * @return {@link VariantParameter} - The {@link VariantParameter} with the specified name, or {@code null} if not found.
     * @author MeAlam
     * @since 1.0.0
     */
    public static VariantParameter getVariantByName(String pEntityName, String pVariantName) {
        BaseLogger.log(BaseLogLevel.INFO, "Retrieving variant by name: " + pVariantName + " for entity: " + pEntityName, true);
        List<VariantParameter> variants = getVariantsFromEntity(pEntityName);
        for (VariantParameter variant : variants) {
            if (variant.getVariantParameter().equals(pVariantName)) {
                return variant;
            }
        }
        BaseLogger.log(BaseLogLevel.INFO, "Variant with name: " + pVariantName + " not found for entity: " + pEntityName, true);
        return null;
    }
}
