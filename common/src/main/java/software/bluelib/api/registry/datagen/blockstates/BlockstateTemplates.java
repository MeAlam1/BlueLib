package software.bluelib.api.registry.datagen.blockstates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;

public abstract class BlockstateTemplates {

	public abstract JsonObject generateBlockstate(String modId, String blockName, Map<String, String> properties);

	public static final BlockstateTemplates SIMPLE_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates ORIENTED_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates VARIANT_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates MULTIPART_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates DOOR_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates FENCE_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates BUTTON_BLOCK = new BlockstateTemplates() {

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
						int yRotation = switch (face) {
							case "ceiling" -> ceilingRotations[0][f];
							case "floor" -> floorRotations[0][f];
							case "wall" -> wallRotations[0][f];
							default -> 0;
						};

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

	public static final BlockstateTemplates SLAB_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates TRAPDOOR_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates STAIRS_BLOCK = new BlockstateTemplates() {

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
							yRotation = switch (shape) {
								case "inner_left", "outer_left" -> bottomRotations[0][facingIndex];
								case "inner_right", "outer_right" -> bottomRotations[1][facingIndex];
								case "straight" -> bottomRotations[1][facingIndex];
								default -> yRotation;
							};
						} else {
							variant.addProperty("x", 180);
							yRotation = switch (shape) {
								case "inner_left", "outer_left" -> topRotations[0][facingIndex];
								case "inner_right", "outer_right" -> topRotations[1][facingIndex];
								case "straight" -> topRotations[1][facingIndex];
								default -> yRotation;
							};
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

	public static final BlockstateTemplates PRESSURE_PLATE_BLOCK = new BlockstateTemplates() {

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

	public static final BlockstateTemplates FENCE_GATE_BLOCK = new BlockstateTemplates() {

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
