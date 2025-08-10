/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import java.util.function.Supplier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.recipe.brewing.BrewingRecipe;

@ApiStatus.Internal
public class BlueRecipeSerializerRegistry {

	public static void init() {}

	@NotNull
	public static final Supplier<RecipeSerializer<?>> BREWING = registerRecipeSerializer("brewing", BrewingRecipe.Serializer::new);

	@NotNull
	@SuppressWarnings("SameParameterValue")
	private static <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(@NotNull String pId, @NotNull Supplier<T> pRecipeSerializer) {
		return BlueLibConstants.PlatformHelper.REGISTRY.registerRecipeSerializer(pId, pRecipeSerializer);
	}
}
