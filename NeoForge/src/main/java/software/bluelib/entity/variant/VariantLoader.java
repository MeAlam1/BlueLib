package software.bluelib.entity.variant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.json.JSONLoader;
import software.bluelib.json.JSONMerger;
import software.bluelib.interfaces.variant.IVariantEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base of the {@link VariantLoader}. <br>
 * {@link VariantLoader} class that loads and manages {@link VariantParameter} for entities by merging JSON data from multiple sources.
 */
public class VariantLoader implements IVariantEntity {

    /**
     * List to store the loaded {@link VariantParameter}.
     */
    private static final List<VariantParameter> variants = new ArrayList<>();

    /**
     * {@link JSONLoader} instance to load JSON data.
     */
    private final JSONLoader jsonLoader = new JSONLoader();

    /**
     * {@link JSONMerger} instance to merge JSON data.
     */
    private final JSONMerger jsonParser = new JSONMerger();

    /**
     * Loads and merges variant data from both the Main Mod and the <strong>Latest</strong> Datapack.
     * Parses the merged data into {@link VariantParameter}.
     *
     * @param pJSONLocationMod  The {@link ResourceLocation} of the Mod's JSON data.
     * @param pJSONLocationPack The {@link ResourceLocation} of the <strong>Latest</strong> DataPack's JSON data.
     * @param pServer           The {@link MinecraftServer} instance.
     */
    public void loadVariants(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();

        JsonObject modJson = jsonLoader.loadJson(pJSONLocationMod, resourceManager);
        JsonObject packJson = jsonLoader.loadJson(pJSONLocationPack, resourceManager);

        JsonObject mergedJsonObject = new JsonObject();
        jsonParser.mergeJsonObjects(mergedJsonObject, modJson);
        jsonParser.mergeJsonObjects(mergedJsonObject, packJson);

        parseVariants(mergedJsonObject);
    }

    /**
     * Parses the merged JSON data and converts it into {@link VariantParameter} instances.
     *
     * @param pJsonObject The merged {@link JsonObject} containing variant data.
     */
    private void parseVariants(JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            JsonArray textureArray = entry.getValue().getAsJsonArray();

            boolean variantExists = variants.stream()
                    .anyMatch(v -> v.getEntityName().equals(entry.getKey()));

            if (!variantExists) {
                for (JsonElement element : textureArray) {
                    VariantParameter variant = getEntityVariant(entry.getKey(), element.getAsJsonObject());
                    variants.add(variant);
                }
            }
        }
    }

    /**
     * Creates a new {@link VariantParameter} instance from a JSON object.
     *
     * @param pJsonKey     The key associated with this variant.
     * @param pJsonObject  The {@link JsonObject} containing the variant data.
     * @return A {@link VariantParameter} instance.
     */
    private static VariantParameter getEntityVariant(String pJsonKey, JsonObject pJsonObject) {
        return new VariantParameter(pJsonKey, pJsonObject);
    }

    /**
     * Retrieves the list of loaded {@link VariantParameter}.
     *
     * @return A {@link List} of {@link VariantParameter} instances.
     */
    public static List<VariantParameter> getVariants() {
        return variants;
    }

    /**
     * Retrieves a {@link VariantParameter} by its name.
     *
     * @param pVariantName The name of the variant to retrieve.
     * @return The {@link VariantParameter} with the specified name, or {@code null} if not found.
     */
    public static VariantParameter getVariantByName(String pVariantName) {
        for (VariantParameter variant : getVariants()) {
            if (variant.getVariantName().equals(pVariantName)) {
                return variant;
            }
        }
        return null;
    }

    /**
     * Gets the name of the variant, which is overridden from {@link IVariantEntity}.
     *
     * @return The name of the variant as a {@link String}.
     */
    @Override
    public String getVariantName() {
        return "EntityName";
    }
}