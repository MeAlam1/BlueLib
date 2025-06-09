/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class NeoRegistries {

	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
			DeferredRegister.create(Registries.RECIPE_TYPE, BlueLibConstants.MOD_ID);

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
			DeferredRegister.create(Registries.RECIPE_SERIALIZER, BlueLibConstants.MOD_ID);

	public static void register(IEventBus pModEventBus) {
		RECIPE_TYPES.register(pModEventBus);
		RECIPE_SERIALIZERS.register(pModEventBus);
	}
}
