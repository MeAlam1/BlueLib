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

public class VariantLoader implements IVariantEntity {

    private static final Gson gson = new Gson();
    private final List<VariantParameter> variants = new ArrayList<>();

    public void loadVariants(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, MinecraftServer pServer) {
        ResourceManager resourceManager = pServer.getResourceManager();
        JsonObject mergedJsonObject = mergeVariantsFromResources(pJSONLocationMod, pJSONLocationPack, resourceManager);
        parseVariants(mergedJsonObject);
    }

    private JsonObject mergeVariantsFromResources(ResourceLocation pJSONLocationMod, ResourceLocation pJSONLocationPack, ResourceManager pResourceManager) {
        JsonObject mergedJsonObject = new JsonObject();

        // Process the first resource
        try {
            Optional<Resource> resourceMod = pResourceManager.getResource(pJSONLocationMod);

            if (resourceMod.isEmpty()) {
                System.out.println("Resource not found, skipping: " + pJSONLocationMod);
            } else {
                System.out.println("Resource found, Not skipping: " + pJSONLocationMod);

                InputStream inputStream = resourceMod.get().open();
                JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
                mergeJsonObjects(mergedJsonObject, jsonObject);
            }
        } catch (CouldNotLoadJSON | IOException e) {
            throw new CouldNotLoadJSON("Failed to parse JSON from resource: " + pJSONLocationMod + ". Error: " + e.getMessage(), pJSONLocationMod.toString());
        }

        // Process the second resource
        try {
            Optional<Resource> resourcePack = pResourceManager.getResource(pJSONLocationPack);

            if (resourcePack.isEmpty()) {
                System.out.println("Resource not found, skipping: " + pJSONLocationPack);
            } else {
                System.out.println("Resource found, Not skipping: " + pJSONLocationPack);

                InputStream inputStream = resourcePack.get().open();
                JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
                mergeJsonObjects(mergedJsonObject, jsonObject);
            }
        } catch (CouldNotLoadJSON | IOException e) {
            throw new CouldNotLoadJSON("Failed to parse JSON from resource: " + pJSONLocationPack + ". Error: " + e.getMessage(), pJSONLocationPack.toString());
        }

        return mergedJsonObject;
    }

    private void mergeJsonObjects(JsonObject target, JsonObject source) {
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            if (target.has(entry.getKey())) {
                // If the key already exists, merge arrays
                JsonArray targetArray = target.getAsJsonArray(entry.getKey());
                JsonArray sourceArray = entry.getValue().getAsJsonArray();

                // Merge arrays
                for (JsonElement element : sourceArray) {
                    targetArray.add(element);
                }
            } else {
                // If the key does not exist, add the new entry
                target.add(entry.getKey(), entry.getValue());
            }
        }
    }

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

    private static VariantParameter getEntityVariant(String pJsonKey, JsonObject pJsonObject) {
        return new VariantParameter(pJsonKey, pJsonObject);
    }

    public List<VariantParameter> getVariants() {
        return variants;
    }

    @Override
    public String getVariantName() {
        return "EntityName";
    }
}
