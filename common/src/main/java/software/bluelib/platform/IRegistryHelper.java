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
import software.bluelib.BlueLibConstants;

public interface IRegistryHelper {

    BlueLibConstants.NetworkManager getNetwork();

    <T extends RecipeType<?>> Supplier<T> registerRecipeType(String pId, Supplier<T> pRecipeType);

    <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String pId, Supplier<T> pRecipeSerializer);

    <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity);

    <T> Supplier<DataComponentType<T>> registerDataComponent(String pId, UnaryOperator<DataComponentType.Builder<T>> pBuilder);
}
