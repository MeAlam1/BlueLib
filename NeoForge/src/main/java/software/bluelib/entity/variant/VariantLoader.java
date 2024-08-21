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
import software.bluelib.interfaces.variant.IVariantEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@code Class} that is the Base for all Variant Loaders.
 * {@link VariantLoader} class that loads and manages {@link VariantParameter} for entities by merging JSON data from multiple sources.
 * @author MeAlam
 */
public class VariantLoader implements IVariantEntityBase {

    /**
     * A {@link Map<String, List<VariantParameter>>} to store loaded {@link VariantParameter} for each entity type.
     */
    private static final Map<String, List<VariantParameter>> entityVariantsMap = new HashMap<>();

    /**
     * A {@link JSONLoader} instance to load JSON data.
     */
    private static final JSONLoader jsonLoader = new JSONLoader();

    /**
     * A {@link JSONMerger} instance to merge JSON data.
     */
    private static final JSONMerger jsonParser = new JSONMerger();

    /**
     * A {@code Void} that loads and merges variant data from both the Main Mod and the <strong>Latest</strong> Datapack.
     * Parses the merged data into {@link VariantParameter}.
     *
     * @param pJSONLocationMod {@link ResourceLocation} - The {@link ResourceLocation} of the Mod's JSON data.
     * @param pJSONLocationPack {@link ResourceLocation} - The {@link ResourceLocation} of the <strong>Latest</strong> DataPack's JSON data.
     * @param pServer {@link MinecraftServer} - The {@link MinecraftServer} instance.
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared before loading new ones.
     * @author MeAlam
     */
    public static void loadVariants(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, MinecraftServer pServer, String pEntityName) {
        clearVariantsForEntity(pEntityName);

        ResourceManager resourceManager = pServer.getResourceManager();

        JsonObject modJson = jsonLoader.loadJson(pJSONLocationMod, resourceManager);
        JsonObject packJson = jsonLoader.loadJson(pJSONLocationPack, resourceManager);

        JsonObject mergedJsonObject = new JsonObject();
        jsonParser.mergeJsonObjects(mergedJsonObject, modJson);
        jsonParser.mergeJsonObjects(mergedJsonObject, packJson);

        parseVariants(mergedJsonObject);
    }

    /**
     * A {@code Void} that clears variants for a specific entity type from the map.
     *
     * @param pEntityName {@link String} - The name of the entity whose variants should be cleared.
     * @author MeAlam
     */
    public static void clearVariantsForEntity(String pEntityName) {
        entityVariantsMap.remove(pEntityName);
    }

    /**
     * A {@code Void} that parses the merged JSON data and converts it into {@link VariantParameter} instances.
     *
     * @param pJsonObject {@link JsonObject} - The merged {@link JsonObject} containing variant data.
     * @author MeAlam
     */
    private static void parseVariants(JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            String entityName = entry.getKey();
            JsonArray textureArray = entry.getValue().getAsJsonArray();

            List<VariantParameter> variantList = entityVariantsMap.computeIfAbsent(entityName, k -> new ArrayList<>());

            for (JsonElement element : textureArray) {
                VariantParameter newVariant = getEntityVariant(entityName, element.getAsJsonObject());

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
     *
     * @param pJsonKey {@link String} - The key associated with this variant.
     * @param pJsonObject {@link JsonObject} - The {@link JsonObject} containing the variant data.
     * @return A {@link VariantParameter} instance.
     * @author MeAlam
     */
    private static VariantParameter getEntityVariant(String pJsonKey, JsonObject pJsonObject) {
        return new VariantParameter(pJsonKey, pJsonObject);
    }

    /**
     * A {@link List<VariantParameter>} that retrieves the {@link List<VariantParameter>} of loaded {@link VariantParameter}
     * for a specific entity.
     *
     * @param pEntityName {@link String} - The name of the entity to retrieve variants for.
     * @return A {@link List<VariantParameter>} of {@link VariantParameter} instances for the specified entity.
     * @author MeAlam
     */
    public static List<VariantParameter> getVariantsForEntity(String pEntityName) {
        return entityVariantsMap.getOrDefault(pEntityName, new ArrayList<>());
    }

    /**
     * A {@link VariantParameter} that retrieves a {@link VariantParameter} by its name for a specific entity.
     *
     * @param pEntityName {@link String} - The name of the entity to retrieve variants for.
     * @param pVariantName {@link String} - The name of the variant to retrieve.
     * @return The {@link VariantParameter} with the specified name, or {@code null} if not found.
     * @author MeAlam
     */
    public static VariantParameter getVariantByName(String pEntityName, String pVariantName) {
        List<VariantParameter> variants = getVariantsForEntity(pEntityName);
        for (VariantParameter variant : variants) {
            if (variant.getVariantName().equals(pVariantName)) {
                return variant;
            }
        }
        return null;
    }
}
