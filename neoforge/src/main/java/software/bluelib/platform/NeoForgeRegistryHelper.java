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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import software.bluelib.BlueLibConstants;
import software.bluelib.NeoRegistries;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.builders.keybinds.KeybindBuilder;
import software.bluelib.api.registry.helpers.entity.AttributeHelper;
import software.bluelib.api.registry.helpers.entity.RenderHelper;
import software.bluelib.net.NeoForgeNetworkManager;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    private static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(Registries.ITEM, AbstractRegistryBuilder.getModID());
    private static final DeferredRegister<Block> blockRegistry = DeferredRegister.create(Registries.BLOCK, AbstractRegistryBuilder.getModID());
    private static final DeferredRegister<EntityType<?>> entityRegistry = DeferredRegister.create(Registries.ENTITY_TYPE, AbstractRegistryBuilder.getModID());
    private static final DeferredRegister<CreativeModeTab> tabRegistry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AbstractRegistryBuilder.getModID());
    private static final DeferredRegister<MenuType<?>> menuRegistry = DeferredRegister.create(Registries.MENU, AbstractRegistryBuilder.getModID());
    private static final DeferredRegister<BlockEntityType<?>> blockEntityRegistry = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AbstractRegistryBuilder.getModID());
    private static final DeferredRegister<Biome> biomeRegistry = DeferredRegister.create(Registries.BIOME, AbstractRegistryBuilder.getModID());

    @Override
    public BlueLibConstants.NetworkManager getNetwork() {
        return new NeoForgeNetworkManager();
    }

	@Override
	public <T extends RecipeType<?>> Supplier<T> registerRecipeType(String pId, Supplier<T> pRecipeType) {
		return NeoRegistries.RECIPE_TYPES.register(pId, pRecipeType);
	}

	@Override
	public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String pId, Supplier<T> pRecipeSerializer) {
		return NeoRegistries.RECIPE_SERIALIZERS.register(pId, pRecipeSerializer);
	}

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerTab(String pId, Supplier<T> pTab) {
        return tabRegistry.register(pId, pTab);
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity) {
        return entityRegistry.register(pId, pEntity);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return itemRegistry.register(id, item);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
        return blockRegistry.register(id, block);
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String pId, Supplier<BlockEntityType<T>> pBlockEntity) {
        return blockEntityRegistry.register(pId, pBlockEntity);
    }

    @Override
    public <T extends MenuType<?>>Supplier<T> registerMenu(String pId, Supplier<T> pMenu) {
        menuRegistry.register(pId, pMenu);
        return pMenu;
    }

    @Override
    public <T extends Biome> Supplier<T> registerBiome(String id, Supplier<T> pBiome) {
        return biomeRegistry.register(id, pBiome);
    }

    @Override
    public Supplier<KeyMapping> registerKeybind(String pId, Supplier<KeyMapping> pKeybind) {
        return pKeybind;
    }

    public static void register(IEventBus modEventBus) {
        entityRegistry.register(modEventBus);
        itemRegistry.register(modEventBus);
        blockRegistry.register(modEventBus);
        tabRegistry.register(modEventBus);
        menuRegistry.register(modEventBus);
        biomeRegistry.register(modEventBus);
        modEventBus.<EntityAttributeCreationEvent>addListener(pEvent -> AttributeHelper.registerAttributes(pEvent::put));
        modEventBus.<RegisterKeyMappingsEvent>addListener(event -> KeybindBuilder.REGISTERED_BUILDERS.forEach(builder -> event.register(builder.getKeyMapping().get())));
        modEventBus.<EntityRenderersEvent.RegisterRenderers>addListener(event -> RenderHelper.registerRenderers(event::registerEntityRenderer, event::registerBlockEntityRenderer));
    }
}
