package software.bluelib.internal.registry;

import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static software.bluelib.BlueLibCommon.REGISTRIES;

public class TestEntityReg {
    public static void init() {
        //BaseLogger.log(BaseLogLevel.SUCCESS, "Registered Entities!");
    }

    public static final Supplier<EntityType<Pig>> TEST_ENTITY = REGISTRIES.entity("test", Pig::new, MobCategory.CREATURE)
            .attributes(Pig::createAttributes)
            .renderer(PigRenderer::new)
            .spawnEgg(0x0000, 0x0000)
            .register();

    public static final Supplier<Block> TEST_BLOCK = REGISTRIES.block("test_block", Block::new)
            .properties(Block.Properties.of().strength(1.0F, 1.0F))
            .defaultItem()
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.getEntry())
                    .requires(Items.DIAMOND, 5)
                    .unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
                    .save(prov))
            //.defaultBlockstate()
            .register();

    public static final Supplier<Item> TEST_ITEM = REGISTRIES.item("test_item", Item::new)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.getEntry())
                    .pattern("XXX")
                    .pattern("XWX")
                    .pattern("XXX")
                    .define('X', Items.RABBIT_FOOT)
                    .define('W', Items.APPLE)
                    .unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
                    .save(prov))
            .register();
}
