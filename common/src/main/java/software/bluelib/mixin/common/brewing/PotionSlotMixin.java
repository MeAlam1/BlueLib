/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.common.brewing;

import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bluelib.recipe.brewing.BrewingRecipe;
import software.bluelib.recipe.brewing.RecipeAwareSlot;

@Mixin(BrewingStandMenu.PotionSlot.class)
public class PotionSlotMixin implements RecipeAwareSlot {

    @Unique
    private RecipeManager bluelib$recipeManager;

    public void blueLib$setRecipeManager(@NotNull RecipeManager pRecipeManager) {
        this.bluelib$recipeManager = pRecipeManager;
    }

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void blueLib$mayPlace(@NotNull ItemStack pStack, @NotNull CallbackInfoReturnable<Boolean> pCir) {
        if (BrewingRecipe.isBottle(pStack, bluelib$recipeManager)) {
            pCir.setReturnValue(true);
        }
    }
}
