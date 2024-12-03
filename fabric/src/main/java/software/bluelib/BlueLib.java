// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import software.bluelib.event.ChatHandler;
import software.bluelib.example.event.ReloadHandler;

/**
 * A {@code public class} that implements {@link ModInitializer} to initialize the BlueLib mod on the Fabric platform.
 * <p>
 * This class handles the initialization of BlueLib by registering a client tick event that ensures
 * the mod is initialized only once during the game runtime.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #onInitialize()} - Registers the client tick event to initialize BlueLib.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @since 1.0.0
 */
public class BlueLib implements ModInitializer {

    /**
     * A {@code private} {@link Boolean} flag indicating whether the mod has been initialized.
     * <p>
     * This ensures that the {@link BlueLibCommon#init()} method is called only once during the game's lifecycle.
     * </p>
     *
     * @since 1.0.0
     */
    private boolean hasInitialized = false;

    /**
     * A {@code public void} that registers a client tick event to initialize the BlueLib mod.
     * <p>
     * This method checks if the mod is being run in developer mode and if the Geckolib mod is loaded. If both conditions
     * are met, it initializes the entities and registers the event listeners for the reload handler.
     * </p>
     * <p>
     * This method uses {@link ClientTickEvents#END_CLIENT_TICK} to register a callback that checks
     * whether the mod has already been initialized and calls {@link BlueLibCommon#init()} if necessary.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void onInitialize() {
        if (BlueLibCommon.isDeveloperMode() && BlueLibConstants.isExampleEnabled) {
            registerModEventListeners();
        }
        registerEventListeners();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!hasInitialized) {
                hasInitialized = true;
                BlueLibCommon.init();
            }
        });
    }

    /**
     * Registers the Mod Dependant event listeners.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public static void registerModEventListeners() {
        ServerLifecycleEvents.SERVER_STARTING.register(ReloadHandler::onServerStart);
    }

    /**
     * Registers the event listeners.
     *
     * @author MeAlam
     * @since 1.2.0
     */
    public static void registerEventListeners() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatHandler::onAllowChat);
    }
}
