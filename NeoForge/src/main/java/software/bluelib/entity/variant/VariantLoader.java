package software.bluelib.entity.variant;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.exception.CouldNotLoadJSON;
import software.bluelib.exception.ResourceNotFound;
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

    public void loadVariants(ResourceLocation pJSONLocation) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        loadVariantsFromJson(pJSONLocation);
        loadVariantsFromResourcePacks(resourceManager, pJSONLocation);
    }

    private void loadVariantsFromResourcePacks(ResourceManager pResourceManager, ResourceLocation pJSONLocation) {

        for (String ignored : pResourceManager.getNamespaces()) {
            try {

                Optional<Resource> resource = pResourceManager.getResource(pJSONLocation);

                if (resource.isEmpty()) {
                    throw new ResourceNotFound("JSON File not Found from resource: " + pJSONLocation, pJSONLocation.toString());
                }

                InputStream inputStream = resource.get().open();
                JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
                parseVariants(jsonObject);

            } catch (CouldNotLoadJSON | IOException e) {
                throw new CouldNotLoadJSON("Failed to parse JSON from resource: " + pJSONLocation.toString(), pJSONLocation.toString());
            }
        }
    }

    private void loadVariantsFromJson(ResourceLocation pJSONLocation) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        try {
            Optional<Resource> resource = resourceManager.getResource(pJSONLocation);

            if (resource.isEmpty()) {
                throw new ResourceNotFound("JSON File not Found from resource: " + pJSONLocation, pJSONLocation.toString());
            }

            InputStream inputStream = resource.get().open();
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
            parseVariants(jsonObject);
        } catch (CouldNotLoadJSON | IOException e) {
            throw new CouldNotLoadJSON("Failed to parse JSON from resource: " + pJSONLocation.toString(), pJSONLocation.toString());
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
