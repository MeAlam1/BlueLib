// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Base class of BlueLib!<br>
 * Welcome, and thank you for using BlueLib!
 * <p>
 * This class serves as the entry point for the BlueLib mod. It initializes the mod by registering event handlers
 * and setting up any necessary configurations. You can find more detailed documentation in the
 * <a href="https://github.com/MeAlam1/BlueLib/wiki">Bluelib Wiki | Introduction</a>.
 *
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">Bluelib Wiki | Introduction</a>
 */
@Mod(BlueLib.MODID)
public class BlueLib {

    /**
     * The Mod ID for BlueLib. Used as a unique identifier for the mod.
     */
    public static final String MODID = "bluelib";

    // public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for the BlueLib class. This constructor registers the mod event bus.
     *
     * @param pModEventBus The event bus to which the mod will register its event handlers.
     */
    public BlueLib(IEventBus pModEventBus) {
        // Register this class to the NeoForge event bus.
        // NeoForge.EVENT_BUS.register(this);

        if (developerMode()) {
            System.out.println("Thanks for using BlueLib!");
        }
    }

    /**
     * Checks if the mod is running in developer mode.
     *
     * @return {@code true} if the mod is not in production mode (i.e., it's in developer mode), {@code false} otherwise.
     */
    static boolean developerMode() {
        return !FMLEnvironment.production;
    }
}
