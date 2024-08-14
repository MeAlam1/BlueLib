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

        mergeResourceIntoJsonObject(pJSONLocationMod, pResourceManager, mergedJsonObject);
        mergeResourceIntoJsonObject(pJSONLocationPack, pResourceManager, mergedJsonObject);

        return mergedJsonObject;
    }

    private void mergeResourceIntoJsonObject(ResourceLocation resourceLocation, ResourceManager resourceManager, JsonObject targetJsonObject) {
        try {
            Optional<Resource> resource = resourceManager.getResource(resourceLocation);

            if (resource.isEmpty()) {
                return;
            }

            try (InputStream inputStream = resource.get().open();
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                mergeJsonObjects(targetJsonObject, jsonObject);
            }
        } catch (CouldNotLoadJSON | IOException e) {
            throw new CouldNotLoadJSON("Failed to parse JSON from resource: " + resourceLocation + ". Error: " + e.getMessage(), resourceLocation.toString());
        }
    }


    private void mergeJsonObjects(JsonObject target, JsonObject source) {
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            if (target.has(entry.getKey())) {
                JsonArray targetArray = target.getAsJsonArray(entry.getKey());
                JsonArray sourceArray = entry.getValue().getAsJsonArray();
                for (JsonElement element : sourceArray) {
                    targetArray.add(element);
                }
            } else {
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
