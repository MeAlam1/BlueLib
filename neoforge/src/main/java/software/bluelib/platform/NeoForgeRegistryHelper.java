/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.function.Supplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.net.NetworkManager;
import software.bluelib.internal.registry.BlueEntityRegistry;
import software.bluelib.net.NeoForgeNetworkManager;

public class NeoForgeRegistryHelper implements IRegistryHelper {

	@NotNull
	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPE = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BlueLibConstants.MOD_ID);
	@NotNull
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BlueLibConstants.MOD_ID);
	@NotNull
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, BlueLibConstants.MOD_ID);
	@NotNull
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, BlueLibConstants.MOD_ID);

	@Override
	public @NotNull NetworkManager getNetwork() {
		return new NeoForgeNetworkManager();
	}

	@Override
	public <T extends RecipeType<?>> @NotNull Supplier<T> registerRecipeType(@NotNull String pId, @NotNull Supplier<T> pRecipeType) {
		return RECIPE_TYPES.register(pId, pRecipeType);
	}

	@Override
	public <T extends Entity> @NotNull Supplier<EntityType<T>> registerEntity(@NotNull String pId, @NotNull Supplier<EntityType<T>> pEntity) {
		return ENTITIES.register(pId, pEntity);
	}

	@Override
	public <T extends DataComponentType<?>> @NotNull Supplier<T> registerDataComponent(@NotNull String pId, @NotNull Supplier<T> pDataComponentType) {
		return DATA_COMPONENT_TYPE.register(pId, pDataComponentType);
	}

	@Override
	public <T extends RecipeSerializer<?>> @NotNull Supplier<T> registerRecipeSerializer(@NotNull String pId, @NotNull Supplier<T> pRecipeSerializer) {
		return RECIPE_SERIALIZERS.register(pId, pRecipeSerializer);
	}

	public static void register(@NotNull IEventBus pModEventBus) {
		RECIPE_TYPES.register(pModEventBus);
		RECIPE_SERIALIZERS.register(pModEventBus);
		DATA_COMPONENT_TYPE.register(pModEventBus);
		ENTITIES.register(pModEventBus);

		pModEventBus.<EntityAttributeCreationEvent>addListener(event -> BlueEntityRegistry.registerEntityAttributes(event::put));
	}
}
