package software.bluelib.api.registry.builders.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.datagen.blocks.*;
import software.bluelib.api.registry.datagen.blockstates.*;
import software.bluelib.api.registry.datagen.items.*;

public class BlockBuilder<T extends Block> {

    public static final List<BlockBuilder<?>> REGISTERED_BUILDERS = new ArrayList<>();
    private static String modId;
    public static String blockName;
    public final Function<Block.Properties, T> blockConstructor;
    public Block.Properties properties;
    public static boolean createDefaultItem = false;
    public T registeredBlock;
    public static BddBlockstateTemplates blockstateTemplate;
    public static BddBlockModelTemplates blockModelTemplate;

    public BlockBuilder(String modID, String name, Function<Block.Properties, T> blockConstructor) {
        modId = modID;
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
                .blockstate(BddBlockstateTemplates.SIMPLE_BLOCK)
                .model(BddBlockModelTemplates.CUBE_ALL)
                .finish();
    }

    public BlockBuilder<T> button() {
        generate(blockName, BddBlockstateTemplates.BUTTON_BLOCK, BddBlockModelTemplates.BUTTON, BddItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        return this;
    }

    public BlockBuilder<T> log() {
        generate(blockName, BddBlockstateTemplates.ORIENTED_BLOCK, BddBlockModelTemplates.COLUMN, BddItemModelTemplates.BLOCK_ITEM);
        return this;
    }

    public BlockBuilder<T> slab() {
        generate(blockName, BddBlockstateTemplates.SLAB_BLOCK, BddBlockModelTemplates.SLAB, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> door() {
        generate(blockName, BddBlockstateTemplates.DOOR_BLOCK, BddBlockModelTemplates.DOOR, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> fence() {
        generate(blockName, BddBlockstateTemplates.FENCE_BLOCK, BddBlockModelTemplates.FENCE, BddItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        BddItemModelGenerator.generateItemModel(modId, blockName, BddItemModelTemplates.GENERATED); // Extra model
        return this;
    }

    public BlockBuilder<T> fenceGate() {
        generate(blockName, BddBlockstateTemplates.FENCE_GATE_BLOCK, BddBlockModelTemplates.FENCE_GATE, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> trapdoor() {
        generate(blockName, BddBlockstateTemplates.TRAPDOOR_BLOCK, BddBlockModelTemplates.TRAPDOOR, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> stairs() {
        generate(blockName, BddBlockstateTemplates.STAIRS_BLOCK, BddBlockModelTemplates.STAIRS, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> pressurePlate() {
        generate(blockName, BddBlockstateTemplates.PRESSURE_PLATE_BLOCK, BddBlockModelTemplates.PRESSURE_PLATE, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    public BlockBuilder<T> tree() {
        generate(blockName + "_log", BddBlockstateTemplates.ORIENTED_BLOCK, BddBlockModelTemplates.COLUMN, BddItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_planks", BddBlockstateTemplates.SIMPLE_BLOCK, BddBlockModelTemplates.CUBE_ALL, BddItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_leaves", BddBlockstateTemplates.SIMPLE_BLOCK, BddBlockModelTemplates.CUBE_ALL, BddItemModelTemplates.BLOCK_ITEM);
        return this;
    }

    public BlockBuilder<T> wood() {
        generate(blockName + "_log", BddBlockstateTemplates.ORIENTED_BLOCK, BddBlockModelTemplates.COLUMN, BddItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_planks", BddBlockstateTemplates.SIMPLE_BLOCK, BddBlockModelTemplates.CUBE_ALL, BddItemModelTemplates.BLOCK_ITEM);
        generate(blockName + "_fence", BddBlockstateTemplates.FENCE_BLOCK, BddBlockModelTemplates.FENCE, BddItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        BddItemModelGenerator.generateItemModel(modId, blockName + "_fence", BddItemModelTemplates.GENERATED); // extra
        generate(blockName + "_door", BddBlockstateTemplates.DOOR_BLOCK, BddBlockModelTemplates.DOOR, BddItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_button", BddBlockstateTemplates.BUTTON_BLOCK, BddBlockModelTemplates.BUTTON, BddItemModelTemplates.BLOCK_WITH_INVENTORY_MODEL);
        generate(blockName + "_slab", BddBlockstateTemplates.SLAB_BLOCK, BddBlockModelTemplates.SLAB, BddItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_pressure_plate", BddBlockstateTemplates.PRESSURE_PLATE_BLOCK, BddBlockModelTemplates.PRESSURE_PLATE, BddItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_stairs", BddBlockstateTemplates.STAIRS_BLOCK, BddBlockModelTemplates.STAIRS, BddItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_trapdoor", BddBlockstateTemplates.TRAPDOOR_BLOCK, BddBlockModelTemplates.TRAPDOOR, BddItemModelTemplates.BLOCK_SPRITE);
        generate(blockName + "_fence_gate", BddBlockstateTemplates.FENCE_GATE_BLOCK, BddBlockModelTemplates.FENCE_GATE, BddItemModelTemplates.BLOCK_SPRITE);
        return this;
    }

    private void generate(String blockName, BddBlockstateTemplates state, BddBlockModelTemplates model, BddItemModelTemplates item) {
        BddBlockstateGenerator.generateBlockstate(modId, blockName, state);
        BddBlockModelGenerator.generateBlockModel(modId, blockName, model);
        BddItemModelGenerator.generateItemModel(modId, blockName, item);
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
            BddItemModelGenerator.generateItemModel(modId, blockName, BddItemModelTemplates.BLOCK_ITEM);
        }
        if (blockstateTemplate != null) {
            BddBlockstateGenerator.generateBlockstate(modId, blockName, blockstateTemplate);
            BddBlockModelGenerator.generateBlockModel(modId, blockName, blockModelTemplate);
        }
    }

    public class BlockstateBuilder {

        public BlockstateBuilder blockstate(BddBlockstateTemplates blockstates) {
            blockstateTemplate = blockstates;
            return this;
        }

        public BlockstateBuilder model(BddBlockModelTemplates models) {
            blockModelTemplate = models;
            return this;
        }

        public BlockBuilder<T> finish() {
            return BlockBuilder.this;
        }
    }
}
