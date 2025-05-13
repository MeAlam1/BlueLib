// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bluelib.BlueLibConstants;
import software.bluelib.net.FabricNetworkManager;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public BlueLibConstants.NetworkManager getNetwork() {
        return new FabricNetworkManager();
    }

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
    public Supplier<MenuType<?>> registerMenu(String pId, Supplier<MenuType<?>> pTab) {
        return registerSupplier(BuiltInRegistries.MENU, pId, pTab);
    }

    /**
     * Quick wrapper to make the individual registration lines cleaner but still return the multiloader-compatible supplier
     */
    private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R pRegistry, String pId, Supplier<T> pObject) {
        final T registeredObject = Registry.register((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());

        return () -> registeredObject;
    }

    /**
     * Quick wrapper to make the individual registration lines cleaner but still return the multiloader-compatible supplier
     */
    private static <T, R extends Registry<? super T>> Holder<T> registerHolder(R pRegistry, String pId, Supplier<T> pObject) {
        return Registry.registerForHolder((Registry<T>) pRegistry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pId), pObject.get());
    }
}
