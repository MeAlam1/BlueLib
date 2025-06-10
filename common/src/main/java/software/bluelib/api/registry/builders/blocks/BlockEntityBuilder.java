package software.bluelib.api.registry.builders.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.helpers.entity.RenderHelper;

public class BlockEntityBuilder<T extends BlockEntity> {

    public static final List<BlockEntityBuilder<?>> REGISTERED_BUILDERS = new ArrayList<>();

    private final String name;
    private final BlockEntityType.BlockEntitySupplier<T> blockEntityFactory;
    private Supplier<BlockEntityRendererProvider<T>> rendererProvider;
    private Supplier<BlockEntityType<T>> blockEntityType;
    private List<Supplier<Block>> validBlockSuppliers;

    public BlockEntityBuilder(String name, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory) {
        this.name = name;
        this.blockEntityFactory = blockEntityFactory;
        this.validBlockSuppliers = new ArrayList<>();
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String name, BlockEntityType.BlockEntitySupplier<T> factory) {
        return new BlockEntityBuilder<>(name, factory);
    }

    public BlockEntityBuilder<T> validBlocks(Supplier<Block>... blockSuppliers) {
        this.validBlockSuppliers = Arrays.asList(blockSuppliers);
        return this;
    }

    public BlockEntityBuilder<T> renderer(BlockEntityRendererProvider<T> renderer) {
        this.rendererProvider = () -> renderer;
        return this;
    }

    public Supplier<BlockEntityType<T>> register() {
        Supplier<BlockEntityType<T>> blockEntityTypeSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerBlockEntity(name, () -> {
            Block[] blocks = validBlockSuppliers.stream()
                    .map(Supplier::get)
                    .toArray(Block[]::new);
            BlockEntityType<T> type = BlockEntityType.Builder.of(blockEntityFactory, blocks).build(null);
            this.blockEntityType = () -> type;
            return type;
        });

        if (rendererProvider != null) {
            RenderHelper.queueRenderer((entityConsumer, blockConsumer) -> {
                blockConsumer.accept(blockEntityType.get(), rendererProvider.get());
            });
        }

        REGISTERED_BUILDERS.add(this);
        return blockEntityTypeSupplier;
    }

    public String getName() {
        return name;
    }

    public Supplier<BlockEntityType<T>> getBlockEntityType() {
        return blockEntityType;
    }
}
