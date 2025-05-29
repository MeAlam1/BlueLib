package software.bluelib.api.registry.builders.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.gui.screens.MenuScreens;
import org.apache.commons.lang3.function.TriFunction;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.helpers.menu.BlueMenuTypeExtension;
import software.bluelib.api.registry.helpers.menu.MenuScreenHelper;

import java.util.function.Supplier;

public class MenuBuilder<T extends AbstractContainerMenu> {
    private static final String modId = AbstractRegistryBuilder.getModID();
    private final String menuId;
    private TriFunction<Integer, Inventory, FriendlyByteBuf, T> menuFactory;
    private MenuScreens.ScreenConstructor<T, ?> screenConstructor;
    private Supplier<MenuType<T>> menuTypeSupplier;

    private MenuBuilder(String menuId) {
        this.menuId = menuId;
    }

    // Create a new builder instance
    public static <T extends AbstractContainerMenu> MenuBuilder<T> menu(String menuId, TriFunction<Integer, Inventory, FriendlyByteBuf, T> factory) {
        MenuBuilder<T> builder = new MenuBuilder<>(menuId);
        builder.menuFactory = factory;
        return builder;
    }

    // Set the screen constructor
    public MenuBuilder<T> screen(MenuScreens.ScreenConstructor<T, ?> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }

    // Register the menu and queue the screen
    public Supplier<MenuType<T>> register() {
        if (menuFactory == null) {
            throw new IllegalStateException("Menu factory must be set before registering");
        }
        // Register the menu using BlueMenuTypeExtension
        this.menuTypeSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerMenu(
                modId + ":" + menuId,
                () -> BlueMenuTypeExtension.create(menuFactory::apply)
        );
        // Queue the screen for registration
        if (screenConstructor != null) {
            MenuScreenHelper.queueScreen(menuTypeSupplier, screenConstructor);
        }
        return menuTypeSupplier;
    }

    // Get the MenuType supplier
    public Supplier<MenuType<T>> getMenuType() {
        if (menuTypeSupplier == null) {
            throw new IllegalStateException("Menu has not been registered yet");
        }
        return menuTypeSupplier;
    }
}