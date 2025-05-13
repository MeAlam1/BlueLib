// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.builders.RegistryBuilder;
import software.bluelib.api.registry.helpers.entity.AttributeHelper;
import software.bluelib.net.NeoForgeNetworkManager;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    private static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(Registries.ITEM, RegistryBuilder.getModID());
    private static final DeferredRegister<Block> blockRegistry = DeferredRegister.create(Registries.BLOCK, RegistryBuilder.getModID());
    private static final DeferredRegister<EntityType<?>> entityRegistry = DeferredRegister.create(Registries.ENTITY_TYPE, RegistryBuilder.getModID());
    private static final DeferredRegister<CreativeModeTab> tabRegistry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RegistryBuilder.getModID());
    private static final DeferredRegister<MenuType<?>> menuRegistry = DeferredRegister.create(Registries.MENU, RegistryBuilder.getModID());

    @Override
    public BlueLibConstants.NetworkManager getNetwork() {
        return new NeoForgeNetworkManager();
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
    public Supplier<MenuType<?>> registerMenu(String pId, Supplier<MenuType<?>> pBlock) {
        menuRegistry.register(pId, pBlock);
        return pBlock;
    }

    public static void register(IEventBus modEventBus) {
        entityRegistry.register(modEventBus);
        itemRegistry.register(modEventBus);
        blockRegistry.register(modEventBus);
        tabRegistry.register(modEventBus);
        menuRegistry.register(modEventBus);
        modEventBus.<EntityAttributeCreationEvent>addListener(pEvent -> AttributeHelper.registerAttributes(pEvent::put));
    }
}
