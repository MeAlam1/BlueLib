package software.bluelib.api.registry.datagen.blocks;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BlockModelTemplates {

    public abstract Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties);

    public static final BlockModelTemplates CUBE_ALL = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:block/cube_all");
            JsonObject textures = new JsonObject();
            String texture = properties.getOrDefault("all", modId + ":block/" + blockName);
            textures.addProperty("all", texture);
            model.add("textures", textures);
            return Collections.singletonMap(blockName, model);
        }
    };

    public static final BlockModelTemplates COLUMN = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:block/cube_column");
            JsonObject textures = new JsonObject();
            String topTexture = properties.getOrDefault("top", modId + ":block/" + blockName + "_top");
            String sideTexture = properties.getOrDefault("side", modId + ":block/" + blockName);
            textures.addProperty("end", topTexture);
            textures.addProperty("side", sideTexture);
            model.add("textures", textures);
            return Collections.singletonMap(blockName, model);
        }
    };

    public static final BlockModelTemplates CUBE = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:block/cube");
            JsonObject textures = new JsonObject();
            textures.addProperty("up", properties.getOrDefault("up", modId + ":block/" + blockName + "_top"));
            textures.addProperty("down", properties.getOrDefault("down", modId + ":block/" + blockName + "_bottom"));
            textures.addProperty("north", properties.getOrDefault("north", modId + ":block/" + blockName));
            textures.addProperty("south", properties.getOrDefault("south", modId + ":block/" + blockName));
            textures.addProperty("east", properties.getOrDefault("east", modId + ":block/" + blockName));
            textures.addProperty("west", properties.getOrDefault("west", modId + ":block/" + blockName));
            model.add("textures", textures);
            return Collections.singletonMap(blockName, model);
        }
    };

    public static final BlockModelTemplates DOOR = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String bottomTexture = properties.getOrDefault("bottom", modId + ":block/" + blockName + "_bottom");
            String topTexture = properties.getOrDefault("top", modId + ":block/" + blockName + "_top");

            String[] halves = { "bottom", "top" };
            String[] hinges = { "left", "right" };
            String[] opens = { "", "_open" };

            for (String half : halves) {
                for (String hinge : hinges) {
                    for (String open : opens) {
                        String modelName = String.format("%s_%s_%s%s", blockName, half, hinge, open);
                        JsonObject model = new JsonObject();
                        String parent = String.format("minecraft:block/door_%s_%s%s", half, hinge, open);
                        model.addProperty("parent", parent);
                        JsonObject textures = new JsonObject();
                        textures.addProperty("bottom", bottomTexture);
                        textures.addProperty("top", topTexture);
                        model.add("textures", textures);
                        models.put(modelName, model);
                    }
                }
            }

            return models;
        }
    };

    public static final BlockModelTemplates FENCE = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String prefix = properties.getOrDefault("prefix", "");
            String s = prefix.isEmpty() ? blockName : prefix + "/" + blockName;
            String texture = properties.getOrDefault("texture", modId + ":block/" + s);

            JsonObject postModel = new JsonObject();
            postModel.addProperty("parent", "minecraft:block/fence_post");
            JsonObject postTextures = new JsonObject();
            postTextures.addProperty("texture", texture);
            postModel.add("textures", postTextures);
            models.put(s + "_fence_post", postModel);

            JsonObject sideModel = new JsonObject();
            sideModel.addProperty("parent", "minecraft:block/fence_side");
            JsonObject sideTextures = new JsonObject();
            sideTextures.addProperty("texture", texture);
            sideModel.add("textures", sideTextures);
            models.put(s + "_fence_side", sideModel);

            JsonObject inventoryModel = new JsonObject();
            inventoryModel.addProperty("parent", "minecraft:block/fence_inventory");
            JsonObject inventoryTextures = new JsonObject();
            inventoryTextures.addProperty("texture", texture);
            inventoryModel.add("textures", inventoryTextures);
            models.put(blockName + "_fence_inventory", inventoryModel);

            return models;
        }
    };

    public static final BlockModelTemplates BUTTON = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String texture = properties.getOrDefault("texture", modId + ":block/" + blockName);

            JsonObject buttonModel = new JsonObject();
            buttonModel.addProperty("parent", "minecraft:block/button");
            JsonObject buttonTextures = new JsonObject();
            buttonTextures.addProperty("texture", texture);
            buttonModel.add("textures", buttonTextures);
            models.put(blockName, buttonModel);

            JsonObject pressedModel = new JsonObject();
            pressedModel.addProperty("parent", "minecraft:block/button_pressed");
            JsonObject pressedTextures = new JsonObject();
            pressedTextures.addProperty("texture", texture);
            pressedModel.add("textures", pressedTextures);
            models.put(blockName + "_pressed", pressedModel);

            JsonObject inventoryModel = new JsonObject();
            inventoryModel.addProperty("parent", "minecraft:block/button_inventory");
            JsonObject inventoryTextures = new JsonObject();
            inventoryTextures.addProperty("texture", texture);
            inventoryModel.add("textures", inventoryTextures);
            models.put(blockName + "_inventory", inventoryModel);

            return models;
        }
    };

    public static final BlockModelTemplates SLAB = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String texture = properties.getOrDefault("texture", modId + ":block/" + blockName.replace("_slab", "_planks"));

            JsonObject slabModel = new JsonObject();
            slabModel.addProperty("parent", "minecraft:block/slab");
            JsonObject slabTextures = new JsonObject();
            slabTextures.addProperty("bottom", texture);
            slabTextures.addProperty("top", texture);
            slabTextures.addProperty("side", texture);
            slabModel.add("textures", slabTextures);
            models.put(blockName, slabModel);

            JsonObject slabTopModel = new JsonObject();
            slabTopModel.addProperty("parent", "minecraft:block/slab_top");
            JsonObject slabTopTextures = new JsonObject();
            slabTopTextures.addProperty("bottom", texture);
            slabTopTextures.addProperty("top", texture);
            slabTopTextures.addProperty("side", texture);
            slabTopModel.add("textures", slabTopTextures);
            models.put(blockName + "_top", slabTopModel);

            return models;
        }
    };

    public static final BlockModelTemplates TRAPDOOR = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String texture = properties.getOrDefault("texture", modId + ":block/" + blockName);

            JsonObject bottomModel = new JsonObject();
            bottomModel.addProperty("parent", "minecraft:block/trapdoor_bottom");
            JsonObject bottomTextures = new JsonObject();
            bottomTextures.addProperty("texture", texture);
            bottomModel.add("textures", bottomTextures);
            models.put(blockName + "_bottom", bottomModel);

            JsonObject topModel = new JsonObject();
            topModel.addProperty("parent", "minecraft:block/trapdoor_top");
            JsonObject topTextures = new JsonObject();
            topTextures.addProperty("texture", texture);
            topModel.add("textures", topTextures);
            models.put(blockName + "_top", topModel);

            JsonObject openModel = new JsonObject();
            openModel.addProperty("parent", "minecraft:block/trapdoor_open");
            JsonObject openTextures = new JsonObject();
            openTextures.addProperty("texture", texture);
            openModel.add("textures", openTextures);
            models.put(blockName + "_open", openModel);

            return models;
        }
    };

    public static final BlockModelTemplates STAIRS = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String texture = properties.getOrDefault("texture", modId + ":block/" + blockName.replace("_stairs", "_planks"));

            JsonObject stairsModel = new JsonObject();
            stairsModel.addProperty("parent", "minecraft:block/stairs");
            JsonObject stairsTextures = new JsonObject();
            stairsTextures.addProperty("bottom", texture);
            stairsTextures.addProperty("top", texture);
            stairsTextures.addProperty("side", texture);
            stairsModel.add("textures", stairsTextures);
            models.put(blockName, stairsModel);

            JsonObject innerModel = new JsonObject();
            innerModel.addProperty("parent", "minecraft:block/inner_stairs");
            JsonObject innerTextures = new JsonObject();
            innerTextures.addProperty("bottom", texture);
            innerTextures.addProperty("top", texture);
            innerTextures.addProperty("side", texture);
            innerModel.add("textures", innerTextures);
            models.put(blockName + "_inner", innerModel);

            JsonObject outerModel = new JsonObject();
            outerModel.addProperty("parent", "minecraft:block/outer_stairs");
            JsonObject outerTextures = new JsonObject();
            outerTextures.addProperty("bottom", texture);
            outerTextures.addProperty("top", texture);
            outerTextures.addProperty("side", texture);
            outerModel.add("textures", outerTextures);
            models.put(blockName + "_outer", outerModel);

            return models;
        }
    };

    public static final BlockModelTemplates PRESSURE_PLATE = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String texture = properties.getOrDefault("texture", modId + ":block/" + blockName);

            JsonObject plateModel = new JsonObject();
            plateModel.addProperty("parent", "minecraft:block/pressure_plate_up");
            JsonObject plateTextures = new JsonObject();
            plateTextures.addProperty("texture", texture);
            plateModel.add("textures", plateTextures);
            models.put(blockName, plateModel);

            JsonObject downModel = new JsonObject();
            downModel.addProperty("parent", "minecraft:block/pressure_plate_down");
            JsonObject downTextures = new JsonObject();
            downTextures.addProperty("texture", texture);
            downModel.add("textures", downTextures);
            models.put(blockName + "_down", downModel);

            return models;
        }
    };

    public static final BlockModelTemplates FENCE_GATE = new BlockModelTemplates() {

        @Override
        public Map<String, JsonObject> generateBlockModel(String modId, String blockName, Map<String, String> properties) {
            Map<String, JsonObject> models = new HashMap<>();
            String texture = properties.getOrDefault("texture", modId + ":block/" + blockName.replace("_fence_gate", "_planks"));

            JsonObject gateModel = new JsonObject();
            gateModel.addProperty("parent", "minecraft:block/fence_gate");
            JsonObject gateTextures = new JsonObject();
            gateTextures.addProperty("texture", texture);
            gateModel.add("textures", gateTextures);
            models.put(blockName, gateModel);

            JsonObject gateOpenModel = new JsonObject();
            gateOpenModel.addProperty("parent", "minecraft:block/fence_gate_open");
            JsonObject gateOpenTextures = new JsonObject();
            gateOpenTextures.addProperty("texture", texture);
            gateOpenModel.add("textures", gateOpenTextures);
            models.put(blockName + "_open", gateOpenModel);

            JsonObject gateWallModel = new JsonObject();
            gateWallModel.addProperty("parent", "minecraft:block/fence_gate_wall");
            JsonObject gateWallTextures = new JsonObject();
            gateWallTextures.addProperty("texture", texture);
            gateWallModel.add("textures", gateWallTextures);
            models.put(blockName + "_wall", gateWallModel);

            JsonObject gateWallOpenModel = new JsonObject();
            gateWallOpenModel.addProperty("parent", "minecraft:block/fence_gate_wall_open");
            JsonObject gateWallOpenTextures = new JsonObject();
            gateWallOpenTextures.addProperty("texture", texture);
            gateWallOpenModel.add("textures", gateWallOpenTextures);
            models.put(blockName + "_wall_open", gateWallOpenModel);

            return models;
        }
    };
}
