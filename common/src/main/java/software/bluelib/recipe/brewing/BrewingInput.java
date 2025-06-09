/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.recipe.brewing;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public class BrewingInput implements RecipeInput {

    private final ItemStack ingredient;
    private final List<ItemStack> bottles;

    public BrewingInput(ItemStack pIngredient, List<ItemStack> pBottles) {
        this.ingredient = pIngredient;
        this.bottles = pBottles;
    }

    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        if (pIndex == 0) {
            return ingredient;
        } else if (pIndex >= 1 && pIndex <= 3) {
            return bottles.size() >= pIndex ? bottles.get(pIndex - 1) : ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1 + bottles.size();
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    public List<ItemStack> getBottles() {
        return bottles;
    }
}
