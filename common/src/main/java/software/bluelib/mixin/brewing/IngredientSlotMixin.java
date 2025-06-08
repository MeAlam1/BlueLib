/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.brewing;

import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bluelib.recipe.brewing.BrewingRecipe;
import software.bluelib.recipe.brewing.RecipeAwareSlot;

@Mixin(BrewingStandMenu.IngredientsSlot.class)
public class IngredientSlotMixin implements RecipeAwareSlot {

    @Unique
    private RecipeManager bluelib$recipeManager;

    @Override
    public void blueLib$setRecipeManager(RecipeManager pRecipeManager) {
        this.bluelib$recipeManager = pRecipeManager;
    }

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void blueLib$mayPlace(ItemStack pStack, CallbackInfoReturnable<Boolean> pCir) {
        if (BrewingRecipe.isInput(pStack, bluelib$recipeManager)) {
            pCir.setReturnValue(true);
        }
    }
}
