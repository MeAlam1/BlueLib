// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.loader.api.FabricLoader;
import software.bluelib.event.ChatHandler;
import software.bluelib.example.event.ReloadHandler;
import software.bluelib.test.TestRegistry;

public class BlueLib implements ModInitializer {

    private boolean hasInitialized = false;

    @Override
    public void onInitialize() {
        registerModEventListeners();
        TestRegistry.registerTests();
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
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ReloadHandler::onReload);
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatHandler::onAllowChat);
    }
}
