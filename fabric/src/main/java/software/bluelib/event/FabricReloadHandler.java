/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.entity.variant.IVariantProvider;

public class FabricReloadHandler {

	public static void registerProvider(@NotNull IVariantProvider pProvider) {
		ReloadHandler.registerProvider(pProvider);
	}

	public static void onServerStart(@NotNull MinecraftServer pServer) {
		ReloadHandler.onServerStart(pServer);
	}

	public static void onReload(@NotNull MinecraftServer pServer, @NotNull CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
		ReloadHandler.onReload(pServer);
	}
}
