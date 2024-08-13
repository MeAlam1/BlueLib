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

    private void LoadVariantsFromJson() {
        /**
         * if (Objects.equals(this.returnEntity(), "") || this.returnEntity() == null) {
         *    throw new EntityNotDefined("Entity name is not defined. The method returnEntity() returned an empty string.");
         * }
         */


        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        try {
            Optional<Resource> resource = resourceManager.getResource(GetVariantResource());
            if (resource.isPresent()) {
                InputStream inputStream = resource.get().open();
                JsonObject jsonObject = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
                ParseVariants(jsonObject);
            } else {
                throw new ResourceNotFound("JSON File not Found. Please check the ResourceLocation thoroughly.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ParseVariants(JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String type = entry.getKey();
            JsonArray textureArray = entry.getValue().getAsJsonArray();

            for (JsonElement element : textureArray) {
                VariantKeys dragonVariants = GetEntityVariant(element, type);
                variants.add(dragonVariants);
            }
        }
    }

    private static VariantKeys GetEntityVariant(JsonElement element, String type) {
        JsonObject textureObject = element.getAsJsonObject();
        String variantName = textureObject.get("VariantName").getAsString();

        return new VariantKeys(variantName, type);
    }

    public List<VariantKeys> GetVariants() {
        return variants;
    }

    @Override
    public ResourceLocation GetVariantResource() {
        return new ResourceLocation(BlueLib.MODID, "bluelib/entity/variant/EntityName.json");
    }

    @Override
    public String GetVariantName() {
        return "EntityName";
    }

    @Override
    public String GetEntityName() {
        return "EntityName";
    }
}
