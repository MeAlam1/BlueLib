package software.bluelib.api.registry.builders.menu;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MenuBuilder<T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> {}
