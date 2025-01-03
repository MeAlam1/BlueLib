// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.platform;

/**
 * A {@code public interface} that defines platform-specific functionality for the BlueLib mod.
 * <p>
 * This interface provides methods to retrieve the platform name, check for loaded mods, and determine
 * if the game is running in a development environment.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #getPlatformName()} - Retrieves the name of the current platform.</li>
 * <li>{@link #isModLoaded(String)} - Checks if a mod is loaded based on its ID.</li>
 * <li>{@link #isDevelopmentEnvironment()} - Determines if the environment is for development.</li>
 * <li>{@link #getEnvironmentName()} - Retrieves the name of the environment type.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface IPlatformHelper {

    /**
     * A {@link String} method that retrieves the name of the current platform.
     *
     * @return The name of the current platform as a {@link String}.
     * @author MeAlam
     * @since 1.0.0
     */
    String getPlatformName();

    /**
     * A {@link Boolean} method that checks if a mod with the given ID is loaded.
     *
     * @param pModId {@link String} - The ID of the mod to check.
     * @return {@code true} if the mod is loaded, {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    boolean isModLoaded(String pModId);

    /**
     * A {@link Boolean} method that checks if the game is currently running in a development environment.
     *
     * @return {@code true} if running in a development environment, {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    boolean isDevelopmentEnvironment();

    /**
     * A {@link String} method that retrieves the name of the current environment type.
     * <p>
     * The environment type is either "development" or "production" based on whether the game is running
     * in a development environment.
     * </p>
     *
     * @return {@link String} - The name of the environment type.
     * @author MeAlam
     * @since 1.0.0
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
