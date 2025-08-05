package software.bluelib.api.registry.builders.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.datagen.recipe.RecipeGenerator;
import software.bluelib.api.registry.datagen.blocks.BlockModelGenerator;
import software.bluelib.api.registry.datagen.blocks.BlockModelTemplates;
import software.bluelib.api.registry.datagen.blockstates.BlockstateGenerator;
import software.bluelib.api.registry.datagen.blockstates.BlockstateTemplates;
import software.bluelib.api.registry.datagen.items.ItemModelGenerator;
import software.bluelib.api.registry.datagen.items.ItemModelTemplates;
import software.bluelib.api.registry.builders.items.ItemBuilder;

public class BlockBuilder<T extends Block> {

    public static final List<BlockBuilder<?>> REGISTERED_BUILDERS = new ArrayList<>();
    private static final String modId = AbstractRegistryBuilder.getModID();
    public final String blockName;
    public final Function<Block.Properties, T> blockConstructor;
    public Block.Properties properties;
    public boolean createDefaultItem = false;
    public T registeredBlock;
    public static BlockstateTemplates blockstateTemplate;
    public static BlockModelTemplates blockModelTemplate;
    private BiConsumer<RecipeContext, RecipeOutput> recipeConsumer;
    private boolean isOre = false;
    private boolean hasRaw = false;
    private boolean hasIngot = false;
    private boolean hasNugget = false;
    private boolean hasDeepslate = false;

    private boolean hasLog = false;
    private boolean hasStrippedLog = false;
    private boolean hasPlanks = false;
    private boolean hasFence = false;
    private boolean hasDoor = false;
    private boolean hasButton = false;
    private boolean hasSlab = false;
    private boolean hasPressurePlate = false;
    private boolean hasStairs = false;
    private boolean hasTrapdoor = false;
    private boolean hasFenceGate = false;
    //private boolean hasSign = false;
    //private boolean hasHangingSign = false;

    public BlockBuilder(String name, Function<Block.Properties, T> blockConstructor) {
        this.blockName = name;
        this.blockConstructor = blockConstructor;
    }

    public BlockBuilder<T> properties(Block.Properties properties) {
        this.properties = properties;
        return this;
    }

    public BlockBuilder<T> defaultItem() {
        this.createDefaultItem = true;
        return this;
    }

    public BlockBuilder<T> defaultBlockstate() {
        return this.datagen()
                .blockstate(BlockstateTemplates.SIMPLE_BLOCK)
                .model(BlockModelTemplates.CUBE_ALL)
                .finish();
    }

    public OreBuilder ore() {
        this.isOre = true;
        return new OreBuilder();
    }

    public WoodBuilder wood() {
        return new WoodBuilder();
    }

    public BlockBuilder<T> recipe(BiConsumer<RecipeContext, RecipeOutput> recipeConsumer) {
        this.recipeConsumer = recipeConsumer;
        return this;
    }

    private void generate(String blockName, BlockstateTemplates state, BlockModelTemplates model, ItemModelTemplates item) {
        BlockstateGenerator.generateBlockstate(modId, blockName, state);
        BlockModelGenerator.generateBlockModel(modId, blockName, model);
        ItemModelGenerator.generateItemModel(modId, blockName, item);
    }

    public BlockstateBuilder datagen() {
        return new BlockstateBuilder();
    }

    public Supplier<Block> register() {
        if (properties == null) {
            properties = Block.Properties.of();
        }

        Supplier<Block> blockSupplier = null;

        if (!isOre) {
            blockSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerBlock(blockName, () -> {
                T block = blockConstructor.apply(properties);
                this.registeredBlock = block;
                return block;
            });

            if (createDefaultItem) {
                Supplier<Item> itemSupplier = () -> new BlockItem(registeredBlock, new Item.Properties());
                BlueLibConstants.PlatformHelper.REGISTRY.registerItem(blockName, itemSupplier);
            }
        }

        if (isOre) {
            blockSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerBlock(blockName + "_block", () -> {
                T block = blockConstructor.apply(properties);
                this.registeredBlock = block;
                return block;
            });

            generate(blockName + "_ore", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);
            generate(blockName + "_block", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);

            if (createDefaultItem) {
                Supplier<Item> itemSupplier = () -> new BlockItem(registeredBlock, new Item.Properties());
                BlueLibConstants.PlatformHelper.REGISTRY.registerItem(blockName + "_block", itemSupplier);
            }

            if (hasDeepslate) {
                generate(blockName + "_deepslate_ore", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);
            }
            if (hasRaw) {
                ItemBuilder.item("raw_" + blockName, Item::new)
                        .model(ItemModelTemplates.GENERATED)
                        .register();
            }
            if (hasIngot) {
                ItemBuilder.item(blockName + "_ingot", Item::new)
                        .model(ItemModelTemplates.GENERATED)
                        .register();
            }
            if (hasNugget) {
                ItemBuilder.item(blockName + "_nugget", Item::new)
                        .model(ItemModelTemplates.GENERATED)
                        .register();
            }
        }

        REGISTERED_BUILDERS.add(this);
        return blockSupplier;
    }

    public static void doBlockModelGen(String modId) {
        for (BlockBuilder<?> builder : REGISTERED_BUILDERS) {
            if (!builder.isOre && builder.createDefaultItem) {
                ItemModelGenerator.generateItemModel(modId, builder.blockName, ItemModelTemplates.BLOCK_ITEM);
            }
            if (!builder.isOre && blockstateTemplate != null) {
                BlockstateGenerator.generateBlockstate(modId, builder.blockName, blockstateTemplate);
                BlockModelGenerator.generateBlockModel(modId, builder.blockName, blockModelTemplate);
            }
        }
    }

    public static void doRecipeGen(String modId) {
        for (BlockBuilder<?> builder : REGISTERED_BUILDERS) {
            if (builder.recipeConsumer != null) {
                RecipeGenerator.generateRecipe(modId, builder.blockName, (recipeOutput, jsonSupplier) -> {
                    RecipeContext ctx = new RecipeContext(builder.registeredBlock);
                    builder.recipeConsumer.accept(ctx, recipeOutput);
                });
            }
        }
    }

    public static class RecipeContext {
        private final Block block;

        public RecipeContext(Block block) {
            this.block = block;
        }

        public Block getEntry() {
            return block;
        }
    }

    public class BlockstateBuilder {
        private final BlockBuilder<T> parent;

        public BlockstateBuilder() {
            this.parent = BlockBuilder.this;
        }

        public BlockstateBuilder blockstate(BlockstateTemplates blockstates) {
            blockstateTemplate = blockstates;
            return this;
        }

        public BlockstateBuilder model(BlockModelTemplates models) {
            blockModelTemplate = models;
            return this;
        }

        public BlockBuilder<T> finish() {
            return parent;
        }
    }

    public class OreBuilder {
        private final BlockBuilder<T> parent;

        public OreBuilder() {
            this.parent = BlockBuilder.this;
        }

        public OreBuilder hasRaw() {
            hasRaw = true;
            return this;
        }

        public OreBuilder hasIngot() {
            hasIngot = true;
            return this;
        }

        public OreBuilder hasNugget() {
            hasNugget = true;
            return this;
        }

        public OreBuilder hasDeepslate() {
            hasDeepslate = true;
            return this;
        }

        public BlockBuilder<T> finish() {
            return parent;
        }
    }

    public class WoodBuilder {
        private final BlockBuilder<T> parent;

        public WoodBuilder() {
            this.parent = BlockBuilder.this;
        }

        public WoodBuilder hasLog() {
            hasLog = true;
            return this;
        }

        public WoodBuilder hasStrippedLog() {
            hasStrippedLog = true;
            return this;
        }

        public WoodBuilder hasPlanks() {
            hasPlanks = true;
            return this;
        }

        public WoodBuilder hasFence() {
            hasFence = true;
            return this;
        }

        public WoodBuilder hasDoor() {
            hasDoor = true;
            return this;
        }

        public WoodBuilder hasButton() {
            hasButton = true;
            return this;
        }

        public WoodBuilder hasSlab() {
            hasSlab = true;
            return this;
        }

        public WoodBuilder hasPressurePlate() {
            hasPressurePlate = true;
            return this;
        }

        public WoodBuilder hasStairs() {
            hasStairs = true;
            return this;
        }

        public WoodBuilder hasTrapdoor() {
            hasTrapdoor = true;
            return this;
        }

        public WoodBuilder hasFenceGate() {
            hasFenceGate = true;
            return this;
        }

        /*public WoodBuilder hasSign() {
            hasSign = true;
            return this;
        }

        public WoodBuilder hasHangingSign() {
            hasHangingSign = true;
            return this;
        }*/

        public BlockBuilder<T> finish() {
            if (hasLog) {
                generate(blockName + "_log", BlockstateTemplates.ORIENTED_BLOCK, BlockModelTemplates.COLUMN, ItemModelTemplates.BLOCK_ITEM);
            }
            if (hasStrippedLog) {
                generate(blockName + "_log_stripped", BlockstateTemplates.ORIENTED_BLOCK, BlockModelTemplates.COLUMN, ItemModelTemplates.BLOCK_ITEM);
            }
            if (hasPlanks) {
                generate(blockName + "_planks", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);
            }
            if (hasFence) {
                generate(blockName + "_fence", BlockstateTemplates.FENCE_BLOCK, BlockModelTemplates.FENCE, ItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
                ItemModelGenerator.generateItemModel(modId, blockName + "_fence", ItemModelTemplates.GENERATED);
            }
            if (hasDoor) {
                generate(blockName + "_door", BlockstateTemplates.DOOR_BLOCK, BlockModelTemplates.DOOR, ItemModelTemplates.BLOCK_SPRITE);
            }
            if (hasButton) {
                generate(blockName + "_button", BlockstateTemplates.BUTTON_BLOCK, BlockModelTemplates.BUTTON, ItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
            }
            if (hasSlab) {
                generate(blockName + "_slab", BlockstateTemplates.SLAB_BLOCK, BlockModelTemplates.SLAB, ItemModelTemplates.BLOCK_SPRITE);
            }
            if (hasPressurePlate) {
                generate(blockName + "_pressure_plate", BlockstateTemplates.PRESSURE_PLATE_BLOCK, BlockModelTemplates.PRESSURE_PLATE, ItemModelTemplates.BLOCK_SPRITE);
            }
            if (hasStairs) {
                generate(blockName + "_stairs", BlockstateTemplates.STAIRS_BLOCK, BlockModelTemplates.STAIRS, ItemModelTemplates.BLOCK_SPRITE);
            }
            if (hasTrapdoor) {
                generate(blockName + "_trapdoor", BlockstateTemplates.TRAPDOOR_BLOCK, BlockModelTemplates.TRAPDOOR, ItemModelTemplates.BLOCK_SPRITE);
            }
            if (hasFenceGate) {
                generate(blockName + "_fence_gate", BlockstateTemplates.FENCE_GATE_BLOCK, BlockModelTemplates.FENCE_GATE, ItemModelTemplates.BLOCK_SPRITE);
            }
            /*if (hasSign) {
                generate(blockName + "_fence_gate", BlockstateTemplates.FENCE_GATE_BLOCK, BlockModelTemplates.FENCE_GATE, ItemModelTemplates.BLOCK_SPRITE);
            }
            if (hasHangingSign) {
                generate(blockName + "_fence_gate", BlockstateTemplates.FENCE_GATE_BLOCK, BlockModelTemplates.FENCE_GATE, ItemModelTemplates.BLOCK_SPRITE);
            }*/

            return parent;
        }
    }
}