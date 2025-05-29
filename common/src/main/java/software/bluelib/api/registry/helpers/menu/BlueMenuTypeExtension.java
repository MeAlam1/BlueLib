package software.bluelib.api.registry.helpers.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface BlueMenuTypeExtension<T> {
    static <T extends AbstractContainerMenu> MenuType<T> create(BlueContainerFactory<T> factory) {
        return new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS);
    }

    T create(int pCreate, Inventory pInventory, RegistryFriendlyByteBuf pBuff);
}
