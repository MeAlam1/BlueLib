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
import software.bluelib.config.ConfigLoader;
import software.bluelib.event.ChatHandler;
import software.bluelib.event.CommandHandler;
import software.bluelib.event.ReloadHandler;

public class FabricEvents {

	public static void register() {
		ServerLifecycleEvents.SERVER_STARTING.register(ReloadHandler::onServerStart);
		ServerLifecycleEvents.SERVER_STARTED.register(ConfigLoader::createConfigs);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ConfigLoader::reloadConfigs);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ReloadHandler::onReload);

		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatHandler::onAllowChat);

		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
	}
}
