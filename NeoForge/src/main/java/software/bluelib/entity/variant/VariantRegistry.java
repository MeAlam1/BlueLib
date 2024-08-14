package software.bluelib.entity.variant;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.BlueLib;
import software.bluelib.exception.ResourceNotFound;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VariantRegistry implements IVariantEntity {

    private static final Gson gson = new Gson();
    private final List<VariantKeys> variants = new ArrayList<>();

    public final void loadVariantsFromJson(ResourceLocation pJSONLocation) {
        /**
         * if (Objects.equals(this.returnEntity(), "") || this.returnEntity() == null) {
         *    throw new EntityNotDefined("Entity name is not defined. The method returnEntity() returned an empty string.");
         * }
         */


        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        try {
            Optional<Resource> resource = resourceManager.getResource(pJSONLocation);
            if (resource.isPresent()) {
                InputStream inputStream = resource.get().open();
                JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
                parseVariants(jsonObject);
            } else {
                throw new ResourceNotFound("JSON File not Found. Please check the ResourceLocation thoroughly.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseVariants(JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> entry : pJsonObject.entrySet()) {
            String type = entry.getKey();
            JsonArray textureArray = entry.getValue().getAsJsonArray();

            for (JsonElement element : textureArray) {
                VariantKeys dragonVariants = getEntityVariant(element, type);
                variants.add(dragonVariants);
            }
        }
    }

    private static VariantKeys getEntityVariant(JsonElement pElement, String pType) {
        JsonObject textureObject = pElement.getAsJsonObject();
        String variantName = textureObject.get("VariantName").getAsString();

        return new VariantKeys(variantName, pType);
    }

    public List<VariantKeys> getVariants() {
        return variants;
    }

    @Override
    public String getVariantName() {
        return "EntityName";
    }

    @Override
    public String getEntityName() {
        return "EntityName";
    }
}
