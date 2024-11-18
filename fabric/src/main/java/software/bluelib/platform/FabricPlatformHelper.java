// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import net.fabricmc.loader.api.FabricLoader;
import software.bluelib.interfaces.platform.IPlatformHelper;

/**
 * A {@code public class} that provides platform-specific implementation for Fabric.
 * <p>
 * This class implements {@link IPlatformHelper} to provide Fabric-specific functionality such as
 * retrieving the platform name, checking if a mod is loaded, and determining if the game is running
 * in a development environment.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getPlatformName()} - Returns the platform name for Fabric.</li>
 *   <li>{@link #isModLoaded(String)} - Checks if a mod is loaded using Fabric's mod loader.</li>
 *   <li>{@link #isDevelopmentEnvironment()} - Checks if Fabric is running in a development environment.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class FabricPlatformHelper implements IPlatformHelper {

    /**
     * A {@code public} {@link String} method that returns the name of the current platform, which is "Fabric" for this implementation.
     *
     * @return {@link String} - The platform name, "Fabric".
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    /**
     * A {@code public} {@link Boolean} method that checks if a mod with the given ID is loaded using Fabric's mod loader.
     *
     * @param pModId {@link String} - The mod ID to check if it's loaded.
     * @return {@code true} if the mod is loaded, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public boolean isModLoaded(String pModId) {
        return FabricLoader.getInstance().isModLoaded(pModId);
    }

    /**
     * A {@code public} {@link Boolean} method that checks if the game is currently running in a development environment
     * using Fabric's environment detection.
     *
     * @return {@code true} if running in a development environment, {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
