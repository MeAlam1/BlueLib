/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import static software.bluelib.BlueLibCommon.REGISTRIES;

import java.util.function.Supplier;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class TestEntityReg {

	public static void init() {
		//BaseLogger.log(BaseLogLevel.SUCCESS, "Registered Entities!");
	}

	public static final Supplier<EntityType<Pig>> TEST_ENTITY = REGISTRIES.livingEntity("test_e", Pig::new, MobCategory.CREATURE)
			.attributes(Pig::createAttributes)
			.renderer(PigRenderer::new)
			.spawnEgg(0x0000, 0x0000)
			.register();

	public static final Supplier<Block> TEST_BLOCK = REGISTRIES.block("test_block", Block::new)
			.properties(Block.Properties.of().strength(1.0F, 1.0F))
			.defaultItem()
			.recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.getEntry())
					.requires(Items.APPLE)
					.unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
					.save(prov))
			.defaultBlockstate()
			.register();

	public static final Supplier<Item> TEST_ITEM = REGISTRIES.item("test_item", Item::new)
			.recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.getEntry(), 5)
					.pattern(" X ")
					.pattern("XWX")
					.pattern(" X ")
					.define('X', Items.RABBIT_FOOT)
					.define('W', Items.APPLE)
					.unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
					.save(prov))
			.register();

	public static final Supplier<Item> TEXT_ITEM = REGISTRIES.item("text_item", Item::new)
			.recipe((ctx, prov) -> SimpleCookingRecipeBuilder.smoking(Ingredient.of(TEST_BLOCK.get()), RecipeCategory.MISC, ctx.getEntry(), 5, 3)
					.unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
					.save(prov))
			.register();

	public static final Supplier<Item> TASTE_ITEM = REGISTRIES.item("taste_item", Item::new)
			.recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.getEntry())
					.requires(Items.CARROT)
					.unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
					.save(prov))
			.register();

	public static final Supplier<Item> TASTE_TEST_ITEM = REGISTRIES.item("taste_test_item", Item::new)
			.recipe((ctx, prov) -> SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.COOKIE), Ingredient.of(Items.COOKIE),
					Ingredient.of(Items.COOKIE), RecipeCategory.MISC, ctx.getEntry())
					.unlocks("has_diamond", RecipeProvider.has(Items.DIAMOND))
					.save(prov, ResourceLocation.fromNamespaceAndPath(REGISTRIES.getModID(), "taste_test_item")))
			.register();
}
