/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import java.util.function.Supplier;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.BlueLibConstants;
import software.bluelib.recipe.brewing.BrewingRecipe;

@ApiStatus.Internal
public class BlueRecipeTypeRegistry {

    public static void init() {}

    public static final Supplier<RecipeType<BrewingRecipe>> BREWING = registerRecipeType("brewing", () -> new RecipeType<>() {});

    private static <T extends RecipeType<?>> Supplier<T> registerRecipeType(String pId, Supplier<T> pRecipeType) {
        return BlueLibConstants.PlatformHelper.REGISTRY.registerRecipeType(pId, pRecipeType);
    }
}
