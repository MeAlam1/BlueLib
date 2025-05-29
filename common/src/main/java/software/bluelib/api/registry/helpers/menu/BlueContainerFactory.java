package software.bluelib.api.registry.helpers.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public interface BlueContainerFactory<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {
    T create(int pCreate, Inventory pInventory, RegistryFriendlyByteBuf pBuff);

    default @NotNull T create(int pCreate, @NotNull Inventory pInventory) {
        return (T)this.create(pCreate, pInventory, null);
    }
}
