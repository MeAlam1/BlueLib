/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.config.ConfigLoader;
import software.bluelib.event.ChatHandler;
import software.bluelib.event.CommandHandler;
import software.bluelib.event.FabricReloadHandler;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.net.messages.client.loader.ControllerCachePacket;

@ApiStatus.Internal
public class FabricEvents {

	public static void register() {
		ServerLifecycleEvents.SERVER_STARTING.register(FabricReloadHandler::onServerStart);
		ServerLifecycleEvents.SERVER_STARTED.register(ConfigLoader::createConfigs);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ConfigLoader::reloadConfigs);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(FabricReloadHandler::onReload);

		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatHandler::onAllowChat);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			NetworkRegistry.sendPacketToPlayer(handler.player, new ControllerCachePacket(ResourceCache.Server.getControllers()));
		});

		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
	}
}
