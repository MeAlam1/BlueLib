/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.recipe.brewing;

import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

public interface RecipeAwareSlot {

	void blueLib$setRecipeManager(@NotNull RecipeManager pRecipeManager);
}
