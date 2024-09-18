// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLib;
import software.bluelib.interfaces.variant.base.IVariantEntityBase;
import software.bluelib.json.JSONLoader;
import software.bluelib.json.JSONMerger;
import software.bluelib.utils.logging.BaseLogger;

import java.util.*;

/**
 * A {@code class} that loads and manages {@link VariantParameter} instances for entities by merging JSON data from multiple sources.
 * <p>
 * The class handles loading of variant data from both the main mod and latest datapack, merging them, and parsing them into {@link VariantParameter} instances.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #loadVariants(String, MinecraftServer, String)} - Loads and merges variant data from both the main mod and the latest datapack.</li>
 *   <li>{@link #getVariantsFromEntity(String)} - Retrieves the list of loaded {@link VariantParameter} for a specific entity.</li>
 *   <li>{@link #getVariantByName(String, String)} - Retrieves a specific {@link VariantParameter} by its name for a given entity.</li>
 * </ul>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class VariantLoader implements IVariantEntityBase {

    private static final Map<String, List<VariantParameter>> entityVariantsMap = new HashMap<>();
    private static final JSONLoader jsonLoader = new JSONLoader();
    private static final JSONMerger jsonMerger = new JSONMerger();

    /**
     * A {@code void} that loads and merges variant data from both the main mod and the latest datapack.
     * <p>
     * This method parses the merged JSON data into {@link VariantParameter} instances and stores them in the internal map.
     * </p>
     * @param folderPath {@link String} - The path to the folder containing JSON resources.
     * @param pServer {@link MinecraftServer} - The {@link MinecraftServer} instance used to access resources.
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared before loading new ones.
     */
    public static void loadVariants(String folderPath, MinecraftServer pServer, String pEntityName) {
        BaseLogger.bluelibLogInfo("Starting to load variants for entity: " + pEntityName);

        clearVariantsForEntity(pEntityName);

        ResourceManager resourceManager = pServer.getResourceManager();
        JsonObject mergedJsonObject = new JsonObject();

        Collection<ResourceLocation> collection = resourceManager.listResources(folderPath, pFiles -> pFiles.getPath().endsWith(".json")).keySet();

        BaseLogger.bluelibLogSuccess("Found resources: " + collection);

        for (ResourceLocation resourceLocation : collection) {
            try {
                BaseLogger.bluelibLogInfo("Loading JSON data from resource: " + resourceLocation.toString());
                JsonObject jsonObject = jsonLoader.loadJson(resourceLocation, resourceManager);
                jsonMerger.mergeJsonObjects(mergedJsonObject, jsonObject);
            } catch (Exception pException) {
                BaseLogger.logError("Failed to load JSON data from resource: " + resourceLocation.toString(), pException);
            }
        }

        BaseLogger.bluelibLogSuccess("Successfully loaded and merged JSON data for entity: " + pEntityName);
        parseVariants(mergedJsonObject);
    }

    /**
     * A {@code void} that clears variants for a specific entity type from the internal map.
     * <p>
     * This method removes all variants associated with the given entity name.
     * </p>
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared.
     */
    private static void clearVariantsForEntity(String pEntityName) {
        BaseLogger.bluelibLogInfo("Clearing variants for entity: " + pEntityName);
        entityVariantsMap.remove(pEntityName);
    }

    /**
     * A {@code void} that parses the merged JSON data and converts it into {@link VariantParameter} instances.
     * <p>
     * This method processes each entry in the JSON object and stores the created {@link VariantParameter} instances in the internal map.
     * </p>
     * @param pJsonObject {@link JsonObject} - The merged {@link JsonObject} containing variant data.
     */
    private static void parseVariants(JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            String entityName = entry.getKey();
            JsonArray textureArray = entry.getValue().getAsJsonArray();

            BaseLogger.bluelibLogInfo("Parsing variants for entity: " + entityName);
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
     * A {@link VariantParameter} that creates a new {@link VariantParameter} instance from a JSON object.
     * <p>
     * This method wraps the creation of {@link VariantParameter} instances for easier management and potential modification.
     * </p>
     * @param pJsonKey {@link String} - The key associated with this variant.
     * @param pJsonObject {@link JsonObject} - The {@link JsonObject} containing the variant data.
     * @return A {@link VariantParameter} instance.
     */
    private static VariantParameter getEntityVariant(String pJsonKey, JsonObject pJsonObject) {
        return new VariantParameter(pJsonKey, pJsonObject);
    }

    /**
     * A {@link List<VariantParameter>} that retrieves the {@link List} of loaded {@link VariantParameter} instances for a specific entity.
     * <p>
     * This method returns a list of variants for the given entity name. If no variants are found, an empty list is returned.
     * </p>
     * @param pEntityName {@link String} - The name of the entity to retrieve variants for.
     * @return A {@link List} of {@link VariantParameter} instances for the specified entity.
     */
    public static List<VariantParameter> getVariantsFromEntity(String pEntityName) {
        BaseLogger.bluelibLogInfo("Retrieving variants for entity: " + pEntityName);
        return entityVariantsMap.getOrDefault(pEntityName, new ArrayList<>());
    }

    /**
     * A {@link VariantParameter} that retrieves a {@link VariantParameter} by its name for a specific entity.
     * <p>
     * This method searches for a variant with the specified name within the list of variants for the given entity.
     * </p>
     * @param pEntityName {@link String} - The name of the entity to retrieve variants for.
     * @param pVariantName {@link String} - The name of the variant to retrieve.
     * @return The {@link VariantParameter} with the specified name, or {@code null} if not found.
     */
    public static VariantParameter getVariantByName(String pEntityName, String pVariantName) {
        BaseLogger.bluelibLogInfo("Retrieving variant by name: " + pVariantName + " for entity: " + pEntityName);
        List<VariantParameter> variants = getVariantsFromEntity(pEntityName);
        for (VariantParameter variant : variants) {
            if (variant.getVariantName().equals(pVariantName)) {
                return variant;
            }
        }
        BaseLogger.logWarning("Variant with name: " + pVariantName + " not found for entity: " + pEntityName);
        return null;
    }
}
