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
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import software.bluelib.BlueLibConstants;
import software.bluelib.internal.Resource;
import software.bluelib.net.FabricNetworkManager;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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

	@Override
	public <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity) {
		return registerSupplier(BuiltInRegistries.ENTITY_TYPE, pId, pEntity);
	}

	@Override
	public <T> Supplier<DataComponentType<T>> registerDataComponent(String pId, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
		final DataComponentType<T> componentType = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Resource.resource(pId).toString(), pBuilder.apply(DataComponentType.builder()).build());

		return () -> componentType;
	}

	private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R pRegistry, String pId, Supplier<T> pObject) {
		final T registeredObject = Registry.register((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());

		return () -> registeredObject;
	}

	private static <T, R extends Registry<? super T>> Holder<T> registerHolder(R pRegistry, String pId, Supplier<T> pObject) {
		return Registry.registerForHolder((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());
	}
}
