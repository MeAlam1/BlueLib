package software.bluelib.api.registry.builders.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.datagen.blocks.BlockModelGenerator;
import software.bluelib.api.registry.datagen.blocks.BlockModelTemplates;
import software.bluelib.api.registry.datagen.blockstates.BlockstateGenerator;
import software.bluelib.api.registry.datagen.blockstates.BlockstateTemplates;
import software.bluelib.api.registry.datagen.items.ItemModelGenerator;
import software.bluelib.api.registry.datagen.items.ItemModelTemplates;
import software.bluelib.api.registry.helpers.blocks.BlockstateBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder<T extends Block> {

    public static final List<BlockBuilder<?>> REGISTERED_BUILDERS = new ArrayList<>();
    private static final String modId = AbstractRegistryBuilder.getModID();
    public static String blockName;
    public final Function<Block.Properties, T> blockConstructor;
    public Block.Properties properties;
    public static boolean createDefaultItem = false;
    public T registeredBlock;
    public static BlockstateTemplates blockstateTemplate;
    public static BlockModelTemplates blockModelTemplate;

    public BlockBuilder(String name, Function<Block.Properties, T> blockConstructor) {
        blockName = name;
        this.blockConstructor = blockConstructor;
    }

    public BlockBuilder<T> properties(Block.Properties properties) {
        this.properties = properties;
        return this;
    }

    public BlockBuilder<T> defaultItem() {
        createDefaultItem = true;
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
        if (createDefaultItem) {
            ItemModelGenerator.generateItemModel(modId, blockName, ItemModelTemplates.BLOCK_ITEM);
        }
        if (blockstateTemplate != null) {
            BlockstateGenerator.generateBlockstate(modId, blockName, blockstateTemplate);
            BlockModelGenerator.generateBlockModel(modId, blockName, blockModelTemplate);
        }
    }
}
