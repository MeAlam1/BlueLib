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
import software.bluelib.interfaces.variant.IVariantEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A {@code Class} that is the Base for all Variant Loaders.
 * {@link VariantLoader} class that loads and manages {@link VariantParameter} for entities by merging JSON data from multiple sources.
 * @author MeAlam
 */
public class VariantLoader implements IVariantEntityBase {

    /**
     * A {@link List<VariantParameter>} to store the loaded {@link VariantParameter}.
     */
    private static final List<VariantParameter> variants = new ArrayList<>();

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
     * @author MeAlam
     */
    public static void loadVariants(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();

        JsonObject modJson = jsonLoader.loadJson(pJSONLocationMod, resourceManager);
        JsonObject packJson = jsonLoader.loadJson(pJSONLocationPack, resourceManager);

        JsonObject mergedJsonObject = new JsonObject();
        jsonParser.mergeJsonObjects(mergedJsonObject, modJson);
        jsonParser.mergeJsonObjects(mergedJsonObject, packJson);

        parseVariants(mergedJsonObject);
    }

    /**
     * A {@code Void} that parses the merged JSON data and converts it into {@link VariantParameter} instances.
     *
     * @param pJsonObject {@link JsonObject} - The merged {@link JsonObject} containing variant data.
     * @author MeAlam
     */
    private static void parseVariants(JsonObject pJsonObject) {
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
     * A {@link List<VariantParameter>} that retrieves the {@link List<VariantParameter>} of loaded {@link VariantParameter}.
     *
     * @return A {@link List<VariantParameter>} of {@link VariantParameter} instances.
     * @author MeAlam
     */
    public static List<VariantParameter> getVariants() {
        return variants;
    }

    /**
     * A {@link VariantParameter} that retrieves a {@link VariantParameter} by its name.
     *
     * @param pVariantName {@link String} - The name of the variant to retrieve.
     * @return The {@link VariantParameter} with the specified name, or {@code null} if not found.
     * @author MeAlam
     */
    public static VariantParameter getVariantByName(String pVariantName) {
        for (VariantParameter variant : getVariants()) {
            if (variant.getVariantName().equals(pVariantName)) {
                return variant;
            }
        }
        return null;
    }
}