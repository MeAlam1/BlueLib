package software.bluelib.api.registry.datagen.items;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Optional;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class BddItemModelTemplates {

    public static final BddItemModelTemplates BLOCK_ITEM = new BddItemModelTemplates() {

        @Override
        public ModelTemplate getTemplate() {
            return new ModelTemplate(
                    Optional.empty(),
                    Optional.empty()) {

                @Override
                public JsonObject createBaseTemplate(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter) {
                    JsonObject jsonObject = new JsonObject();
                    String blockName = modelLocation.getPath().replace("item/", "block/");
                    jsonObject.addProperty("parent", modelLocation.getNamespace() + ":" + blockName);
                    return jsonObject;
                }
            };
        }
    };

    public static final BddItemModelTemplates BLOCK_WITH_INVENTORY_MODEL = new BddItemModelTemplates() {

        @Override
        public ModelTemplate getTemplate() {
            return new ModelTemplate(Optional.empty(), Optional.empty()) {

                @Override
                public JsonObject createBaseTemplate(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter) {
                    JsonObject jsonObject = new JsonObject();
                    String path = modelLocation.getPath().startsWith("item/")
                            ? modelLocation.getPath().substring(5)
                            : modelLocation.getPath();
                    String blockName = path + "_fence_inventory";
                    jsonObject.addProperty("parent", modelLocation.getNamespace() + ":block/" + blockName);
                    return jsonObject;
                }
            };
        }
    };

    public static final BddItemModelTemplates BLOCK_SPRITE = new BddItemModelTemplates() {

        @Override
        public ModelTemplate getTemplate() {
            return new ModelTemplate(
                    Optional.of(ResourceLocation.withDefaultNamespace("item/generated")),
                    Optional.empty(),
                    TextureSlot.LAYER0) {

                @Override
                public JsonObject createBaseTemplate(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("parent", "minecraft:item/generated");
                    JsonObject textures = new JsonObject();
                    ResourceLocation texture = modelGetter.getOrDefault(TextureSlot.LAYER0,
                            ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), modelLocation.getPath().replace("item/", "block/")));
                    textures.addProperty("layer0", texture.toString());
                    jsonObject.add("textures", textures);
                    return jsonObject;
                }
            };
        }
    };

    public static final BddItemModelTemplates GENERATED = new BddItemModelTemplates() {

        @Override
        public ModelTemplate getTemplate() {
            return new ModelTemplate(
                    Optional.of(ResourceLocation.withDefaultNamespace("item/generated")),
                    Optional.empty(),
                    TextureSlot.LAYER0) {

                @Override
                public JsonObject createBaseTemplate(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("parent", "minecraft:item/generated");
                    JsonObject textures = new JsonObject();
                    int layerCount = 0;
                    for (TextureSlot slot : modelGetter.keySet()) {
                        if (slot == TextureSlot.LAYER0 || slot.getId().startsWith("layer")) {
                            textures.addProperty("layer" + layerCount, modelGetter.get(slot).toString());
                            layerCount++;
                        }
                    }
                    if (!modelGetter.containsKey(TextureSlot.LAYER0)) {
                        textures.addProperty("layer0", modelLocation.getNamespace() + ":item/" + modelLocation.getPath().replace("item/", ""));
                    }
                    jsonObject.add("textures", textures);
                    return jsonObject;
                }
            };
        }
    };

    public static final BddItemModelTemplates HANDHELD = new BddItemModelTemplates() {

        @Override
        public ModelTemplate getTemplate() {
            return new ModelTemplate(
                    Optional.of(ResourceLocation.withDefaultNamespace("item/handheld")),
                    Optional.empty(),
                    TextureSlot.LAYER0) {

                @Override
                public JsonObject createBaseTemplate(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("parent", "minecraft:item/handheld");
                    JsonObject textures = new JsonObject();
                    ResourceLocation texture = modelGetter.getOrDefault(TextureSlot.LAYER0,
                            ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), modelLocation.getPath()));
                    textures.addProperty("layer0", texture.toString());
                    jsonObject.add("textures", textures);
                    return jsonObject;
                }
            };
        }
    };

    public static final BddItemModelTemplates SPAWN_EGG = new BddItemModelTemplates() {

        @Override
        public ModelTemplate getTemplate() {
            return new ModelTemplate(
                    Optional.of(ResourceLocation.fromNamespaceAndPath("minecraft", "item/template_spawn_egg")),
                    Optional.empty()) {

                @Override
                public @NotNull JsonObject createBaseTemplate(@NotNull ResourceLocation modelLocation, @NotNull Map<TextureSlot, ResourceLocation> modelGetter) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("parent", "minecraft:item/template_spawn_egg");
                    return jsonObject;
                }
            };
        }
    };

    public abstract ModelTemplate getTemplate();
}
