/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.utils;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

public class ResourceUtils {

	public static @NotNull ResourceManager getResourceManager() {
		return Minecraft.getInstance().getResourceManager();
	}

	public static @NotNull Optional<Resource> getResource(@NotNull ResourceLocation pResourcePath) {
		return getResourceManager().getResource(pResourcePath);
	}
}
