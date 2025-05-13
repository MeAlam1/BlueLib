package software.bluelib.api.registry.datagen.blockstates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public abstract class BddBlockstateTemplates {

    public abstract JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties);

    public static final BddBlockstateTemplates SIMPLE_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            JsonObject variant = new JsonObject();
            variant.addProperty("model", modId + ":block/" + blockName);
            variants.add("", variant);
            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates ORIENTED_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();

            String model = modId + ":block/" + blockName;
            String horizontalModel = properties.getOrDefault("horizontal_model", model);

            JsonObject xVariant = new JsonObject();
            xVariant.addProperty("model", horizontalModel);
            xVariant.addProperty("x", 90);
            xVariant.addProperty("y", 90);
            variants.add("axis=x", xVariant);

            JsonObject yVariant = new JsonObject();
            yVariant.addProperty("model", model);
            variants.add("axis=y", yVariant);

            JsonObject zVariant = new JsonObject();
            zVariant.addProperty("model", horizontalModel);
            zVariant.addProperty("x", 90);
            variants.add("axis=z", zVariant);

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates VARIANT_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String propertyName = properties.getOrDefault("property", "type");
            String[] values = properties.getOrDefault("values", "default").split(",");
            for (String value : values) {
                JsonObject variant = new JsonObject();
                variant.addProperty("model", modId + ":block/" + blockName + "_" + value.trim());
                variants.add(propertyName + "=" + value.trim(), variant);
            }
            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates MULTIPART_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonArray multipart = new JsonArray();

            JsonObject basePart = new JsonObject();
            basePart.addProperty("model", modId + ":block/" + blockName);
            multipart.add(basePart);

            String[] directions = { "north", "east", "south", "west" };
            for (String dir : directions) {
                JsonObject part = new JsonObject();
                JsonObject when = new JsonObject();
                when.addProperty(dir, "true");
                part.add("when", when);
                JsonObject apply = new JsonObject();
                apply.addProperty("model", modId + ":block/" + blockName + "_" + dir);
                part.add("apply", apply);
                multipart.add(part);
            }

            blockstate.add("multipart", multipart);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates DOOR_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();

            String modelPrefix = properties.getOrDefault("model_prefix", modId + ":block/" + blockName);

            String[] facings = { "east", "north", "south", "west" };
            String[] halves = { "lower", "upper" };
            String[] hinges = { "left", "right" };
            String[] opens = { "false", "true" };

            int[][] rotations = {
                    { 0, 90, 270 },
                    { 270, 0, 180 },
                    { 90, 180, 0 },
                    { 180, 270, 90 }
            };

            for (int f = 0; f < facings.length; f++) {
                String facing = facings[f];
                for (String half : halves) {
                    for (String hinge : hinges) {
                        for (String open : opens) {
                            String variantKey = String.format("facing=%s,half=%s,hinge=%s,open=%s", facing, half, hinge, open);
                            JsonObject variant = new JsonObject();

                            String modelSuffix = String.format("%s_%s%s",
                                    half.equals("lower") ? "bottom" : "top",
                                    hinge,
                                    open.equals("true") ? "_open" : "");
                            String model = modelPrefix + "_" + modelSuffix;
                            variant.addProperty("model", model);

                            int rotationIndex = open.equals("true") ? (hinge.equals("left") ? 1 : 2) : 0;
                            int yRotation = rotations[f][rotationIndex];
                            if (yRotation != 0) {
                                variant.addProperty("y", yRotation);
                            }

                            variants.add(variantKey, variant);
                        }
                    }
                }
            }

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates FENCE_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonArray multipart = new JsonArray();
            String prefix = properties.getOrDefault("prefix", "");
            String modelPath = prefix.isEmpty() ? modId + ":block/" + blockName : modId + ":block/" + prefix + "/" + blockName;

            JsonObject postPart = new JsonObject();
            postPart.addProperty("model", modelPath + "_fence_post");
            multipart.add(postPart);

            String[] directions = { "north", "east", "south", "west" };
            int[] rotations = { 0, 90, 180, 270 };
            for (int i = 0; i < directions.length; i++) {
                String dir = directions[i];
                JsonObject part = new JsonObject();
                JsonObject when = new JsonObject();
                when.addProperty(dir, "true");
                part.add("when", when);
                JsonObject apply = new JsonObject();
                apply.addProperty("model", modelPath + "_fence_side");
                apply.addProperty("uvlock", true);
                if (rotations[i] != 0) {
                    apply.addProperty("y", rotations[i]);
                }
                part.add("apply", apply);
                multipart.add(part);
            }

            blockstate.add("multipart", multipart);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates BUTTON_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String[] faces = { "ceiling", "floor", "wall" };
            String[] facings = { "east", "north", "south", "west" };
            String[] powered = { "false", "true" };
            int[][] ceilingRotations = { { 270, 180, 0, 90 } }; // east, north, south, west
            int[][] floorRotations = { { 90, 0, 180, 270 } };
            int[][] wallRotations = { { 90, 0, 180, 270 } };

            for (String face : faces) {
                for (int f = 0; f < facings.length; f++) {
                    String facing = facings[f];
                    for (String power : powered) {
                        String variantKey = String.format("face=%s,facing=%s,powered=%s", face, facing, power);
                        JsonObject variant = new JsonObject();
                        String model = power.equals("true") ? modId + ":block/" + blockName + "_pressed" : modId + ":block/" + blockName;
                        variant.addProperty("model", model);

                        int xRotation = face.equals("ceiling") ? 180 : face.equals("wall") ? 90 : 0;
                        int yRotation = 0;
                        if (face.equals("ceiling")) {
                            yRotation = ceilingRotations[0][f];
                        } else if (face.equals("floor")) {
                            yRotation = floorRotations[0][f];
                        } else if (face.equals("wall")) {
                            yRotation = wallRotations[0][f];
                        }

                        if (xRotation != 0) {
                            variant.addProperty("x", xRotation);
                        }
                        if (yRotation != 0) {
                            variant.addProperty("y", yRotation);
                        }
                        if (face.equals("wall")) {
                            variant.addProperty("uvlock", true);
                        }

                        variants.add(variantKey, variant);
                    }
                }
            }

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates SLAB_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String doubleModel = properties.getOrDefault("double_model", modId + ":block/" + blockName.replace("_slab", "_planks"));

            JsonObject bottomVariant = new JsonObject();
            bottomVariant.addProperty("model", modId + ":block/" + blockName);
            variants.add("type=bottom", bottomVariant);

            JsonObject topVariant = new JsonObject();
            topVariant.addProperty("model", modId + ":block/" + blockName + "_top");
            variants.add("type=top", topVariant);

            JsonObject doubleVariant = new JsonObject();
            doubleVariant.addProperty("model", doubleModel);
            variants.add("type=double", doubleVariant);

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates TRAPDOOR_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String[] facings = { "east", "north", "south", "west" };
            String[] halves = { "bottom", "top" };
            String[] opens = { "false", "true" };
            int[][] rotations = { { 90, 0, 180, 270 }, { 270, 180, 0, 90 } }; // bottom/top: east, north, south, west

            for (String facing : facings) {
                for (String half : halves) {
                    for (String open : opens) {
                        String variantKey = String.format("facing=%s,half=%s,open=%s", facing, half, open);
                        JsonObject variant = new JsonObject();
                        String model;
                        if (open.equals("true")) {
                            model = modId + ":block/" + blockName + "_open";
                        } else {
                            model = modId + ":block/" + blockName + (half.equals("bottom") ? "_bottom" : "_top");
                        }
                        variant.addProperty("model", model);

                        int yRotation = rotations[half.equals("bottom") ? 0 : 1][java.util.Arrays.asList(facings).indexOf(facing)];
                        if (yRotation != 0) {
                            variant.addProperty("y", yRotation);
                        }
                        if (open.equals("true") && half.equals("top")) {
                            variant.addProperty("x", 180);
                        }

                        variants.add(variantKey, variant);
                    }
                }
            }

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates STAIRS_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String[] facings = { "east", "north", "south", "west" };
            String[] halves = { "bottom", "top" };
            String[] shapes = { "inner_left", "inner_right", "outer_left", "outer_right", "straight" };
            int[][] bottomRotations = { { 0, 270, 90, 180 }, { 270, 0, 180, 90 } }; // inner_left/inner_right, outer_left/outer_right: east, north, south, west
            int[][] topRotations = { { 0, 270, 90, 180 }, { 90, 0, 180, 270 } };

            for (String facing : facings) {
                for (String half : halves) {
                    for (String shape : shapes) {
                        String variantKey = String.format("facing=%s,half=%s,shape=%s", facing, half, shape);
                        JsonObject variant = new JsonObject();
                        String modelSuffix = shape.startsWith("inner") ? "_inner" : shape.startsWith("outer") ? "_outer" : "";
                        variant.addProperty("model", modId + ":block/" + blockName + modelSuffix);

                        int facingIndex = java.util.Arrays.asList(facings).indexOf(facing);
                        int yRotation = 0;
                        boolean uvlock = !shape.equals("straight");

                        if (half.equals("bottom")) {
                            if (shape.equals("inner_left") || shape.equals("outer_left")) {
                                yRotation = bottomRotations[0][facingIndex];
                            } else if (shape.equals("inner_right") || shape.equals("outer_right")) {
                                yRotation = bottomRotations[1][facingIndex];
                            } else if (shape.equals("straight")) {
                                yRotation = bottomRotations[1][facingIndex];
                            }
                        } else {
                            variant.addProperty("x", 180);
                            if (shape.equals("inner_left") || shape.equals("outer_left")) {
                                yRotation = topRotations[0][facingIndex];
                            } else if (shape.equals("inner_right") || shape.equals("outer_right")) {
                                yRotation = topRotations[1][facingIndex];
                            } else if (shape.equals("straight")) {
                                yRotation = topRotations[1][facingIndex];
                            }
                        }

                        if (yRotation != 0) {
                            variant.addProperty("y", yRotation);
                        }
                        if (uvlock) {
                            variant.addProperty("uvlock", true);
                        }

                        variants.add(variantKey, variant);
                    }
                }
            }

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates PRESSURE_PLATE_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();

            JsonObject unpoweredVariant = new JsonObject();
            unpoweredVariant.addProperty("model", modId + ":block/" + blockName);
            variants.add("powered=false", unpoweredVariant);

            JsonObject poweredVariant = new JsonObject();
            poweredVariant.addProperty("model", modId + ":block/" + blockName + "_down");
            variants.add("powered=true", poweredVariant);

            blockstate.add("variants", variants);
            return blockstate;
        }
    };

    public static final BddBlockstateTemplates FENCE_GATE_BLOCK = new BddBlockstateTemplates() {

        @Override
        public JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties) {
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String[] facings = { "east", "north", "south", "west" };
            String[] inWalls = { "false", "true" };
            String[] opens = { "false", "true" };
            int[] yRotations = { 270, 180, 0, 90 }; // east, north, south, west

            for (int f = 0; f < facings.length; f++) {
                String facing = facings[f];
                for (String inWall : inWalls) {
                    for (String open : opens) {
                        String variantKey = String.format("facing=%s,in_wall=%s,open=%s", facing, inWall, open);
                        JsonObject variant = new JsonObject();
                        String modelSuffix = inWall.equals("true") ? "_wall" : "";
                        modelSuffix += open.equals("true") ? "_open" : "";
                        String model = modId + ":block/" + blockName + modelSuffix;
                        variant.addProperty("model", model);
                        variant.addProperty("uvlock", true);
                        if (yRotations[f] != 0) {
                            variant.addProperty("y", yRotations[f]);
                        }
                        variants.add(variantKey, variant);
                    }
                }
            }

            blockstate.add("variants", variants);
            return blockstate;
        }
    };
}
