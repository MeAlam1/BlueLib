/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.function.Supplier;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity) {
        return registerSupplier(BuiltInRegistries.ENTITY_TYPE, pId, pEntity);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerTab(String pId, Supplier<T> pTab) {
        return registerSupplier(BuiltInRegistries.CREATIVE_MODE_TAB, pId, pTab);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return registerSupplier(BuiltInRegistries.ITEM, id, item);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String pId, Supplier<T> pBlock) {
        return registerSupplier(BuiltInRegistries.BLOCK, pId, pBlock);
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String pId, Supplier<BlockEntityType<T>> pBlockEntity) {
        return registerSupplier(BuiltInRegistries.BLOCK_ENTITY_TYPE, pId, pBlockEntity);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenu(String pId, Supplier<T> pMenu) {
        return registerSupplier(BuiltInRegistries.MENU, pId, pMenu);
    }

    @Override
    public <T extends Biome> Supplier<T> registerBiome(String pId, Supplier<T> pMenu) {
        return pMenu/*registerSupplier(BuiltInRegistries.BIOME, pId, pMenu)*/;
    }

    @Override
    public Supplier<KeyMapping> registerKeybind(String pId, Supplier<KeyMapping> pKeybind) {
        KeyMapping keyMapping = pKeybind.get();
        KeyBindingHelper.registerKeyBinding(keyMapping);
        return () -> keyMapping;
    }

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
