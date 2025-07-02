/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.net.FabricNetworkManager;

@SuppressWarnings({ "unchecked", "unused" })
public class FabricRegistryHelper implements IRegistryHelper {

	@Override
	public @NotNull NetworkManager getNetwork() {
		return new FabricNetworkManager();
	}

	@Override
	public <T extends RecipeType<?>> @NotNull Supplier<T> registerRecipeType(@NotNull String pId, @NotNull Supplier<T> pRecipeType) {
		return registerSupplier(BuiltInRegistries.RECIPE_TYPE, pId, pRecipeType);
	}

	@Override
	public <T extends RecipeSerializer<?>> @NotNull Supplier<T> registerRecipeSerializer(@NotNull String pId, @NotNull Supplier<T> pRecipeSerializer) {
		return registerSupplier(BuiltInRegistries.RECIPE_SERIALIZER, pId, pRecipeSerializer);
	}

	@NotNull
	private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(@NotNull R pRegistry, @NotNull String pId, @NotNull Supplier<T> pObject) {
		final T registeredObject = Registry.register((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());

		return () -> registeredObject;
	}

	@NotNull
	private static <T, R extends Registry<? super T>> Holder<T> registerHolder(@NotNull R pRegistry, @NotNull String pId, @NotNull Supplier<T> pObject) {
		return Registry.registerForHolder((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());
	}
}
