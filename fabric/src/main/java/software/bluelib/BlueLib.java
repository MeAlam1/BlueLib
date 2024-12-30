// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.fabricmc.api.ModInitializer;

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
 * @version 1.7.0
 * @since 1.7.0
 */
public class BlueLib implements ModInitializer {

    /**
     * A {@code public void} that registers a client tick event to initialize the BlueLib mod.
     *
     * @author MeAlam
     * @since 1.7.0
     */
    @Override
    public void onInitialize() {}
}
