// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.exception.CouldNotLoadJSON;
import software.bluelib.interfaces.variant.IVariantEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Base of the Variant Loader. <br>
 * Loads and manages variant parameters for entities by merging JSON data from multiple sources.
 */
public class VariantLoader implements IVariantEntity {

    /**
     * Gson instance for parsing JSON data.
     */
    private static final Gson gson = new Gson();

    /**
     * List to store the loaded variant parameters.
     */
    private static final List<VariantParameter> variants = new ArrayList<>();

    /**
     * Loads and merges variant data from both the Main Mod and the <strong>Latest</strong> Datapack. <br>
     * Parses the merged data into variant parameters
     *
     * @param pJSONLocationMod  The {@link ResourceLocation} of the Mod's JSON data.
     * @param pJSONLocationPack The {@link ResourceLocation} of the <strong>Latest</strong> DataPack's JSON data.
     * @param pServer           The {@link MinecraftServer} instance.
     */
    public void loadVariants(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();
        JsonObject mergedJsonObject = mergeVariantsToObject(pJSONLocationMod, pJSONLocationPack, resourceManager);
        parseVariants(mergedJsonObject);
    }

    /**
     * Merges JSON data from the Main Mod and the <strong>Latest</strong> Datapack resource locations into a single {@link JsonObject}.
     *
     * @param pJSONLocationMod   The {@link ResourceLocation} of the Mod's JSON data.
     * @param pJSONLocationPack  The {@link ResourceLocation} of the <strong>Latest</strong> DataPack's JSON data.
     * @param pResourceManager   The {@link ResourceManager} to load the resources.
     * @return The merged {@link JsonObject} containing data from both sources.
     */
    private JsonObject mergeVariantsToObject(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, ResourceManager pResourceManager) {
        JsonObject mergedJsonObject = new JsonObject();

        loadVariantsFromJson(pJSONLocationMod, pResourceManager, mergedJsonObject);
        loadVariantsFromJson(pJSONLocationPack, pResourceManager, mergedJsonObject);

        return mergedJsonObject;
    }

    /**
     * Loads JSON data from a resource location and merges it into the target {@link JsonObject}.
     *
     * @param pResourceLocation  The {@link ResourceLocation} of the JSON resource.
     * @param pResourceManager   The {@link ResourceManager} to load the resource.
     * @param pTargetJsonObject  The target {@link JsonObject} to merge the data into.
     */
    private void loadVariantsFromJson(ResourceLocation pResourceLocation, ResourceManager pResourceManager, JsonObject pTargetJsonObject) {
        try {
            Optional<Resource> resource = pResourceManager.getResource(pResourceLocation);

            if (resource.isEmpty()) {
                return;
            }

            try (InputStream inputStream = resource.get().open();
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                mergeModAndDataVariants(pTargetJsonObject, jsonObject);
            }
        } catch (CouldNotLoadJSON | IOException e) {
            throw new CouldNotLoadJSON("Failed to parse JSON from resource: " + pResourceLocation + ". Error: " + e.getMessage(), pResourceLocation.toString());
        }
    }

    /**
     * Merges data from a source {@link JsonObject} into a target {@link JsonObject}. <br>
     * If the key exists in the target, the source data is appended to the existing data.
     *
     * @param pTarget  The target {@link JsonObject} to merge data into.
     * @param pSource  The source {@link JsonObject} to merge data from.
     */
    private void mergeModAndDataVariants(JsonObject pTarget, JsonObject pSource) {
        for (Map.Entry<String, JsonElement> entry : pSource.entrySet()) {
            if (pTarget.has(entry.getKey())) {
                JsonArray targetArray = pTarget.getAsJsonArray(entry.getKey());
                JsonArray sourceArray = entry.getValue().getAsJsonArray();
                for (JsonElement element : sourceArray) {
                    targetArray.add(element);
                }
            } else {
                pTarget.add(entry.getKey(), entry.getValue());
            }
        }
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
     * Retrieves the list of loaded variant parameters.
     *
     * @return A {@link List} of {@link VariantParameter} instances.
     */
    public static List<VariantParameter> getVariants() {
        return variants;
    }

    /**
     * Retrieves a {@link VariantParameter} by its name.
     *
     * @param variantName The name of the variant to retrieve.
     * @return The {@link VariantParameter} with the specified name, or {@code null} if not found.
     */
    public static VariantParameter getVariantByName(String variantName) {
        for (VariantParameter variant : getVariants()) {
            if (variant.getVariantName().equals(variantName)) {
                return variant;
            }
        }
        return null;
    }

    /**
     * Gets the name of the variant, which is overridden from {@link IVariantEntity}.
     *
     * @return The name of the variant as a string.
     */
    @Override
    public String getVariantName() {
        return "EntityName";
    }
}
