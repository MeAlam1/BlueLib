/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import software.bluelib.BlueLibConstants;
import software.bluelib.net.FabricNetworkManager;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

	@Override
	public BlueLibConstants.NetworkManager getNetwork() {
		return new FabricNetworkManager();
	}

	@Override
	public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String pId, Supplier<T> pRecipeType) {
		return registerSupplier(BuiltInRegistries.RECIPE_TYPE, pId, pRecipeType);
	}

	@Override
	public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String pId, Supplier<T> pRecipeSerializer) {
		return registerSupplier(BuiltInRegistries.RECIPE_SERIALIZER, pId, pRecipeSerializer);
	}

	private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R pRegistry, String pId, Supplier<T> pObject) {
		final T registeredObject = Registry.register((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());

		return () -> registeredObject;
	}

	private static <T, R extends Registry<? super T>> Holder<T> registerHolder(R pRegistry, String pId, Supplier<T> pObject) {
		return Registry.registerForHolder((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());
	}
}
