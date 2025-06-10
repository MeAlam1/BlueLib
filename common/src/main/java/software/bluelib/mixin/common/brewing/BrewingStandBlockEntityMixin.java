/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.common.brewing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.internal.registry.BlueRecipeTypeRegistry;
import software.bluelib.recipe.brewing.BrewingInput;
import software.bluelib.recipe.brewing.BrewingRecipe;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {

    @WrapOperation(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BrewingStandBlockEntity;doBrew(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/NonNullList;)V"))
    private static void blueLib$doBrew(Level pLevel, BlockPos pPos, NonNullList<ItemStack> pSlots, Operation<Void> pOriginal) {
        var recipe = blueLib$fetchBrewingRecipe(pSlots, pLevel);
        if (recipe == null) {
            pOriginal.call(pLevel, pPos, pSlots);
            return;
        }
        ItemStack result = recipe.getResult().copy();
        for (int i = 0; i < 3; ++i) {
            pSlots.set(i, result);
        }

        ItemStack ingredient = pSlots.get(3);
        ingredient.shrink(1);
        Item craftingRemaining = ingredient.getItem().getCraftingRemainingItem();
        if (ingredient.getItem().hasCraftingRemainingItem() && craftingRemaining != null) {
            ItemStack rem = new ItemStack(craftingRemaining);
            if (ingredient.isEmpty()) {
                ingredient = rem;
            } else {
                Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), rem);
            }
        }

        pSlots.set(3, ingredient);
        pLevel.levelEvent(LevelEvent.SOUND_BREWING_STAND_BREW, pPos, 0);
    }

    @WrapOperation(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BrewingStandBlockEntity;isBrewable(Lnet/minecraft/world/item/alchemy/PotionBrewing;Lnet/minecraft/core/NonNullList;)Z"))
    private static boolean blueLib$isBrewable(PotionBrewing pPotionBrewing, NonNullList<ItemStack> pItems, Operation<Boolean> pOriginal,
            @Local(argsOnly = true) Level pLevel) {
        return blueLib$fetchBrewingRecipe(pItems, pLevel) != null || pOriginal.call(pPotionBrewing, pItems);
    }

    @Unique
    private static BrewingRecipe blueLib$fetchBrewingRecipe(NonNullList<ItemStack> pItems, Level pLevel) {
        ItemStack ingredient = pItems.get(3);
        List<ItemStack> bottles = pItems.subList(0, 3);

        boolean allBottlesEmpty = true;
        for (ItemStack bottle : bottles) {
            if (!bottle.isEmpty()) {
                allBottlesEmpty = false;
                break;
            }
        }

        if (ingredient.isEmpty() || allBottlesEmpty) {
            return null;
        }

        BrewingInput input = new BrewingInput(ingredient, bottles);
        RecipeManager recipeManager = pLevel.getRecipeManager();

        Optional<RecipeHolder<BrewingRecipe>> recipeHolder = recipeManager.getRecipeFor(BlueRecipeTypeRegistry.BREWING.get(), input, pLevel);

        return recipeHolder.map(RecipeHolder::value).orElse(null);
    }
}
