/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import software.bluelib.BlueLib;
import software.bluelib.BlueLibConstants;
import software.bluelib.net.NeoForgeNetworkManager;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class NeoForgeRegistryHelper implements IRegistryHelper {

	@Override
	public BlueLibConstants.NetworkManager getNetwork() {
		return new NeoForgeNetworkManager();
	}

	@Override
	public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String pId, Supplier<T> pRecipeType) {
		return BlueLib.RECIPE_TYPES.register(pId, pRecipeType);
	}

	@Override
	public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String pId, Supplier<T> pRecipeSerializer) {
		return BlueLib.RECIPE_SERIALIZERS.register(pId, pRecipeSerializer);
	}

	@Override
	public <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity) {
		return BlueLib.ENTITIES.register(pId, pEntity);
	}

	@Override
	public <T> Supplier<DataComponentType<T>> registerDataComponent(String pId, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
		return BlueLib.DATA_COMPONENTS_REGISTER.registerComponentType(pId, pBuilder);
	}
}
