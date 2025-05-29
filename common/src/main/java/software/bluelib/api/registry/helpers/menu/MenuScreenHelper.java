package software.bluelib.api.registry.helpers.menu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MenuScreenHelper {
    /** List to hold all queued screen registrations. */
    private static final List<ScreenRegistration<?>> SCREEN_REGISTRATIONS = new ArrayList<>();

    /** Inner class to store menu type and screen constructor pairs. */
    private static class ScreenRegistration<T extends AbstractContainerMenu> {
        final Supplier<MenuType<T>> menuType;
        final MenuScreens.ScreenConstructor<T, ?> screenConstructor;

        ScreenRegistration(
                Supplier<MenuType<T>> menuType,
                MenuScreens.ScreenConstructor<T, ?> screenConstructor
        ) {
            this.menuType = menuType;
            this.screenConstructor = screenConstructor;
        }
    }

    /**
     * Queues a menu screen for registration.
     *
     * @param menuType Supplier providing the MenuType<T>
     * @param screenConstructor ScreenConstructor for the menu
     * @param <T> Type of the menu extending AbstractContainerMenu
     */
    public static <T extends AbstractContainerMenu> void queueScreen(
            Supplier<MenuType<T>> menuType,
            MenuScreens.ScreenConstructor<T, ?> screenConstructor) {
        SCREEN_REGISTRATIONS.add(new ScreenRegistration<>(menuType, screenConstructor));
    }

    /**
     * Registers all queued screens using the provided registrar.
     *
     * @param registrar BiConsumer that accepts a MenuType and ScreenConstructor
     */
    public static void registerScreens(BiConsumer<MenuType<?>, MenuScreens.ScreenConstructor<?, ?>> registrar) {
        for (ScreenRegistration<?> registration : SCREEN_REGISTRATIONS) {
            registrar.accept(registration.menuType.get(), registration.screenConstructor);
        }
    }
}
