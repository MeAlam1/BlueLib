/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({ "unused" })
public class ScreenUtils {

	public static boolean isScreenOpen() {
		return Minecraft.getInstance().screen != null;
	}

	public static @Nullable Screen getCurrentScreen() {
		return Minecraft.getInstance().screen;
	}

	public static boolean isGamePaused() {
		return Minecraft.getInstance().isPaused();
	}

	public static void closeCurrentScreen() {
		Minecraft.getInstance().setScreen(null);
	}
}
