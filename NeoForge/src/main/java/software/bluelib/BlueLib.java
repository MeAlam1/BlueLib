// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * The Base {@code Class} of {@link BlueLib}!<br>
 * Welcome, and thank you for using {@link BlueLib}!
 * <p>
 * This class serves as the entry point for the {@link BlueLib} mod. It initializes the mod by registering event handlers
 * and setting up any necessary configurations. You can find more detailed documentation in the <br>
 * <a href="https://github.com/MeAlam1/BlueLib/wiki">Bluelib Wiki | Introduction</a>.
 *
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">Bluelib Wiki | Introduction</a>
 * @author MeAlam
 */
@Mod(BlueLib.MODID)
public class BlueLib {

    /**
     * The Mod ID for {@link BlueLib}. Used as a unique identifier for the mod.
     */
    public static final String MODID = "bluelib";

    // public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for the {@link BlueLib} class. This constructor registers the mod event bus.
     *
     * @param pModEventBus {@link IEventBus} - The event bus to which the mod will register its event handlers.
     * @author MeAlam
     */
    public BlueLib(IEventBus pModEventBus) {
        // Register this class to the NeoForge event bus.
        // NeoForge.EVENT_BUS.register(this);

        if (developerMode()) {
            System.out.println("Thanks for using BlueLib!");
        }
    }

    /**
     * A {@link Boolean} that checks if the mod is running in developer mode.
     *
     * @return {@code true} if the mod is running in developer mode, {@code false} otherwise.
     * @author MeAlam
     */
    static boolean developerMode() {
        return !FMLEnvironment.production;
    }
}
