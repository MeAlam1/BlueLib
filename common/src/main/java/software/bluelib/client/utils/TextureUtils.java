/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TextureUtils {

	public static @NotNull TextureManager getTextureManager() {
		return Minecraft.getInstance().getTextureManager();
	}

	public static @NotNull AbstractTexture getTexture(@NotNull ResourceLocation pTexturePath) {
		return getTextureManager().getTexture(pTexturePath);
	}
}
