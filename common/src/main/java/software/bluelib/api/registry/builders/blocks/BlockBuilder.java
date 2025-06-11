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

    public BlockBuilder<T> button() {
        generate(blockName, BlockstateTemplates.BUTTON_BLOCK, BlockModelTemplates.BUTTON, ItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        return this;
    }

    public BlockBuilder<T> log() {
        generate(blockName, BlockstateTemplates.ORIENTED_BLOCK, BlockModelTemplates.COLUMN, ItemModelTemplates.BLOCK_ITEM);
        return this;
    }

    public BlockBuilder<T> slab() {
        generate(blockName, BlockstateTemplates.SLAB_BLOCK, BlockModelTemplates.SLAB, ItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> door() {
        generate(blockName, BlockstateTemplates.DOOR_BLOCK, BlockModelTemplates.DOOR, ItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> fence() {
        generate(blockName, BlockstateTemplates.FENCE_BLOCK, BlockModelTemplates.FENCE, ItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        ItemModelGenerator.generateItemModel(modId, blockName, ItemModelTemplates.GENERATED); // Extra model
        return this;
    }

    public BlockBuilder<T> fenceGate() {
        generate(blockName, BlockstateTemplates.FENCE_GATE_BLOCK, BlockModelTemplates.FENCE_GATE, ItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> trapdoor() {
        generate(blockName, BlockstateTemplates.TRAPDOOR_BLOCK, BlockModelTemplates.TRAPDOOR, ItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> stairs() {
        generate(blockName, BlockstateTemplates.STAIRS_BLOCK, BlockModelTemplates.STAIRS, ItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> pressurePlate() {
        generate(blockName, BlockstateTemplates.PRESSURE_PLATE_BLOCK, BlockModelTemplates.PRESSURE_PLATE, ItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> tree() {
        generate(blockName + "_log", BlockstateTemplates.ORIENTED_BLOCK, BlockModelTemplates.COLUMN, ItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_planks", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_leaves", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);
        return this;
    }

    public BlockBuilder<T> wood() {
        generate(blockName + "_log", BlockstateTemplates.ORIENTED_BLOCK, BlockModelTemplates.COLUMN, ItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_planks", BlockstateTemplates.SIMPLE_BLOCK, BlockModelTemplates.CUBE_ALL, ItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_fence", BlockstateTemplates.FENCE_BLOCK, BlockModelTemplates.FENCE, ItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        ItemModelGenerator.generateItemModel(modId, blockName + "_fence", ItemModelTemplates.GENERATED); // extra
        generate(blockName + "_door", BlockstateTemplates.DOOR_BLOCK, BlockModelTemplates.DOOR, ItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_button", BlockstateTemplates.BUTTON_BLOCK, BlockModelTemplates.BUTTON, ItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        generate(blockName + "_slab", BlockstateTemplates.SLAB_BLOCK, BlockModelTemplates.SLAB, ItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_pressure_plate", BlockstateTemplates.PRESSURE_PLATE_BLOCK, BlockModelTemplates.PRESSURE_PLATE, ItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_stairs", BlockstateTemplates.STAIRS_BLOCK, BlockModelTemplates.STAIRS, ItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_trapdoor", BlockstateTemplates.TRAPDOOR_BLOCK, BlockModelTemplates.TRAPDOOR, ItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_fence_gate", BlockstateTemplates.FENCE_GATE_BLOCK, BlockModelTemplates.FENCE_GATE, ItemModelTemplates.BLOCK_SPRITE);
        return this;
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

        Supplier<Block> blockSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerBlock(blockName, () -> {
            T block = blockConstructor.apply(properties);
            this.registeredBlock = block;
            return block;
        });

        if (createDefaultItem) {
            Supplier<Item> itemSupplier = () -> new BlockItem(registeredBlock, new Item.Properties());
            BlueLibConstants.PlatformHelper.REGISTRY.registerItem(blockName, itemSupplier);
        }

        REGISTERED_BUILDERS.add(this);
        return blockSupplier;
    }

    public static void doBlockModelGen(String modId) {
        for (BlockBuilder<?> builder : REGISTERED_BUILDERS) {
            if (builder.createDefaultItem) {
                ItemModelGenerator.generateItemModel(modId, builder.blockName, ItemModelTemplates.BLOCK_ITEM);
            }
            if (blockstateTemplate != null) {
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
}
