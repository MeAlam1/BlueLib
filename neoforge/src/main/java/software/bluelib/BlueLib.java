// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * The main class of the {@code BlueLib} mod.
 * <p>
 * This class serves as the entry point for the {@code BlueLib} mod, handling initialization by registering event handlers
 * and setting up necessary configurations. For more details, refer to the <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #BlueLib(IEventBus, ModContainer)} - Constructs the {@code BlueLib} instance and registers the mod event bus.</li>
 * </ul>
 *
 * @author MeAlam, Dan and All Contributors of BlueLib!
 * @version 1.7.0
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 * @since 1.7.0
 */
@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    /**
     * Constructs a new {@code BlueLib} instance and registers the mod event bus.
     * <p>
     * Registers necessary mod event listeners, and if in developer mode, additional client-side listeners for rendering and attributes.
     * </p>
     *
     * @param pModEventBus  {@link IEventBus} - The event bus where the mod registers its handlers.
     * @param pModContainer {@link ModContainer} - The mod container that holds the instance of the mod.
     * @author MeAlam
     * @since 1.7.0
     */
    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {}
}
