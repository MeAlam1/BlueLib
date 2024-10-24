// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import software.bluelib.interfaces.platform.IPlatformHelper;

/**
 * A {@code public class} that provides platform-specific implementation for Forge.
 * <p>
 * This class implements {@link IPlatformHelper} to provide Forge-specific functionality such as
 * retrieving the platform name, checking if a mod is loaded, and determining if the game is running
 * in a development environment.
 * </p>
 *
 * Key Methods:
 * <ul>
 *   <li>{@link #getPlatformName()} - Returns the platform name for Forge.</li>
 *   <li>{@link #isModLoaded(String)} - Checks if a mod is loaded using Forge's {@link ModList}.</li>
 *   <li>{@link #isDevelopmentEnvironment()} - Checks if Forge is running in a development environment.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class ForgePlatformHelper implements IPlatformHelper {

    /**
     * A {@code public} {@link String} method that returns the name of the current platform, which is "Forge" for this implementation.
     *
     * @return {@link String} - The platform name, "Forge".
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    /**
     * A {@code public} {@link Boolean} method that checks if a mod with the given ID is loaded using Forge's {@link ModList}.
     *
     * @param pModId {@link String} - The mod ID to check if it's loaded.
     * @return {@code true} if the mod is loaded, {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public boolean isModLoaded(String pModId) {
        return ModList.get().isLoaded(pModId);
    }

    /**
     * A {@code public} {@link Boolean} method that checks if the game is currently running in a development environment
     * using Forge's {@link FMLLoader}.
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
