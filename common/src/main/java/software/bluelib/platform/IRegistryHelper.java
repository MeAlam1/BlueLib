/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

	@NotNull
	<T extends Entity> Supplier<EntityType<T>> registerEntity(@NotNull String pId, @NotNull Supplier<EntityType<T>> pEntity);

	@NotNull
	<T> Supplier<DataComponentType<T>> registerDataComponent(@NotNull String pId, @NotNull UnaryOperator<DataComponentType.Builder<T>> pBuilder);
}
