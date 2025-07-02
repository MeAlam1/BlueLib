/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.compat.jei.brewing;

import java.util.List;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bluelib.recipe.brewing.BrewingRecipe;

public class BrewingJeiRecipe implements IJeiBrewingRecipe {

	private final BrewingRecipe recipe;
	private final ResourceLocation id;

	public BrewingJeiRecipe(@NotNull BrewingRecipe pRecipe, @NotNull ResourceLocation pId) {
		this.recipe = pRecipe;
		this.id = pId;
	}

	@Override
	public @NotNull List<ItemStack> getPotionInputs() {
		return List.of(recipe.getBottleIngredient().getItems());
	}

	@Override
	public @NotNull List<ItemStack> getIngredients() {
		return List.of(recipe.getInputIngredient().getItems());
	}

	@Override
	public @NotNull ItemStack getPotionOutput() {
		return recipe.getResult().copy();
	}

	@Override
	public int getBrewingSteps() {
		return 1;
	}

	@Override
	@NotNull
	public ResourceLocation getUid() {
		return id;
	}
}
