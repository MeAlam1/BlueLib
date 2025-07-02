/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.function.Supplier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkManager;

public interface IRegistryHelper {

	@NotNull
	NetworkManager getNetwork();

	@NotNull
	<T extends RecipeType<?>> Supplier<T> registerRecipeType(@NotNull String pId, @NotNull Supplier<T> pRecipeType);

	@NotNull
	<T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(@NotNull String pId, @NotNull Supplier<T> pRecipeSerializer);
}
