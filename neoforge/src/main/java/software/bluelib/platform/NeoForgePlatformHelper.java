// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import software.bluelib.interfaces.platform.IPlatformHelper;

/**
 * A {@link NeoForgePlatformHelper} class that provides platform-specific implementation for NeoForge.
 * <p>
 * This class implements {@link IPlatformHelper} to provide NeoForge-specific functionality such as
 * retrieving the platform name, checking if a mod is loaded, and determining if the game is running
 * in a development environment.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getPlatformName()} - Returns the platform name for NeoForge.</li>
 *   <li>{@link #isModLoaded(String)} - Checks if a mod is loaded using NeoForge's {@link ModList}.</li>
 *   <li>{@link #isDevelopmentEnvironment()} - Checks if NeoForge is running in a development environment.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class NeoForgePlatformHelper implements IPlatformHelper {

    /**
     * A {@code public} {@link String} method that returns the name of the current platform, which is "NeoForge" for this implementation.
     *
     * @return {@link String} - The platform name, "NeoForge".
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    /**
     * A {@code public} {@link Boolean} method that checks if a mod with the given ID is loaded using NeoForge's {@link ModList}.
     *
     * @param pModId {@link String} - The mod ID to check if it's loaded.
     * @return {@code true} if the mod is loaded, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public boolean isModLoaded(String pModId) {
        return ModList.get().isLoaded(pModId);
    }

    /**
     * A {@code public} {@link Boolean} method that checks if the game is currently running in a development environment
     * using NeoForge's {@link FMLLoader}.
     *
     * @return {@code true} if running in a development environment, {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
