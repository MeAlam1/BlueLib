package software.bluelib.api.registry.helpers.blocks;

import software.bluelib.api.registry.builders.blocks.BlockBuilder;
import software.bluelib.api.registry.datagen.blocks.BlockModelTemplates;
import software.bluelib.api.registry.datagen.blockstates.BlockstateTemplates;
import static software.bluelib.api.registry.builders.blocks.BlockBuilder.*;

public class BlockstateBuilder {
    public BlockstateBuilder blockstate(BlockstateTemplates blockstates) {
        blockstateTemplate = blockstates;
        return this;
    }

    public BlockstateBuilder model(BlockModelTemplates models) {
        blockModelTemplate = models;
        return this;
    }

    public BlockBuilder finish() {
        return this.finish();
    }
}
