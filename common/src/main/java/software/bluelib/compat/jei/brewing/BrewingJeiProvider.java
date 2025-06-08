/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.compat.jei.brewing;

import java.util.List;
import java.util.stream.Collectors;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeManager;
import software.bluelib.compat.jei.BlueJeiProvider;
import software.bluelib.registry.BlueRecipeTypeRegistry;

public class BrewingJeiProvider implements BlueJeiProvider {

    @Override
    public void registerCategory(IRecipeCategoryRegistration pRegistration) {}

    @Override
    public void registerRecipes(IRecipeRegistration pRegistration) {
        RecipeManager recipeManager = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getRecipeManager()
                : null;

        if (recipeManager == null) {
            throw new IllegalStateException("Recipe manager not found");
        }

        List<IJeiBrewingRecipe> jeiRecipes = recipeManager.getAllRecipesFor(BlueRecipeTypeRegistry.BREWING.get())
                .stream()
                .map(recipe -> new BrewingJeiRecipe(recipe.value(), recipe.id()))
                .collect(Collectors.toList());

        pRegistration.addRecipes(RecipeTypes.BREWING, jeiRecipes);
    }
}
