/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.function.Supplier;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import software.bluelib.BlueLibConstants;

public interface IRegistryHelper {

    BlueLibConstants.NetworkManager getNetwork();

    <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity);

    <T extends CreativeModeTab> Supplier<T> registerTab(String pId, Supplier<T> pTab);

    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> pItem);

    <T extends Block> Supplier<T> registerBlock(String pId, Supplier<T> pBlock);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String pId, Supplier<BlockEntityType<T>> pBlockEntity);

    <T extends MenuType<?>>Supplier<T> registerMenu(String pId, Supplier<T> pMenu);

    <T extends Biome> Supplier<T> registerBiome(String pId, Supplier<T> pBiome);

   Supplier<KeyMapping> registerKeybind(String pId, Supplier<KeyMapping> pKeybind);

    <T extends RecipeType<?>> Supplier<T> registerRecipeType(String pId, Supplier<T> pRecipeType);

    <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String pId, Supplier<T> pRecipeSerializer);
}
