/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.loader.api.FabricLoader;
import software.bluelib.config.ConfigLoader;
import software.bluelib.event.ChatHandler;
import software.bluelib.event.CommandHandler;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.FabricNetworkManager;

public class BlueLib implements ModInitializer {

    private boolean hasInitialized = false;

    @Override
    public void onInitialize() {
        ReloadHandler.registerProvider(new VariantProvider());
        BlueLibCommon.doRegistration();
        FabricNetworkManager.registerMessages();
        FabricNetworkManager.registerServerHandlers();
        registerModEventListeners();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!hasInitialized) {
                    hasInitialized = true;
                    BlueLibCommon.init();
                }
            });
        }
    }

    public static void registerModEventListeners() {
        ServerLifecycleEvents.SERVER_STARTING.register(ReloadHandler::onServerStart);
        ServerLifecycleEvents.SERVER_STARTED.register(ConfigLoader::createConfigs);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ConfigLoader::reloadConfigs);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ReloadHandler::onReload);
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatHandler::onAllowChat);
        CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
    }
}
