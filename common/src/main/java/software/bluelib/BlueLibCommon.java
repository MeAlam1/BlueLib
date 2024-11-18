// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import software.bluelib.interfaces.platform.IPlatformHelper;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import static software.bluelib.BlueLibConstants.SCHEDULER;

/**
 * A {@code public class} responsible for common initialization logic and platform detection.
 * <p>
 * This class contains utility methods to load services, initialize the mod in developer mode,
 * and determine if the current environment is a development environment.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #init()} - Initializes BlueLib and logs welcome messages if in developer mode.</li>
 *   <li>{@link #isDeveloperMode()} - Checks if the mod is running in developer mode.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlueLibCommon {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private BlueLibCommon() {
    }

    /**
     * A {@code public static final} {@link IPlatformHelper} instance that represents the current platform helper loaded for the mod.
     * This is used to identify platform-specific functionalities.
     *
     * @since 1.0.0
     */
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    /**
     * A {@code public static} {@link T} that loads a service using {@link ServiceLoader}.
     *
     * @param pClazz {@link Class} - The class type of the service to load.
     * @param <T>    The type of the service class to be loaded.
     * @return The loaded service instance of type {@code T}.
     * @throws NullPointerException if the service cannot be found.
     * @author MeAlam
     * @since 1.0.0
     */
    public static <T> T load(Class<T> pClazz) {
        return ServiceLoader.load(pClazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + pClazz.getName()));
    }

    /**
     * A {@code public static void} that initializes BlueLib and logs a thank-you message if in developer mode. <br>
     * The message is scheduled to appear after 5 seconds and then the scheduler shuts down.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public static void init() {
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                BaseLogger.logBlueLib("**************************************************");
                BaseLogger.logBlueLib("                                                  ");
                BaseLogger.logBlueLib("     Thank you for using BlueLib!                 ");
                BaseLogger.logBlueLib("     We appreciate your support.                  ");
                BaseLogger.logBlueLib("                                                  ");
                BaseLogger.logBlueLib("**************************************************");
                SCHEDULER.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    /**
     * A {@code public static} {@link Boolean} that checks if the mod is running in developer mode.
     * <p>
     * Developer mode is active when the mod is not running in a production environment.
     * </p>
     *
     * @return {@code true} if running in developer mode, {@code false} if it isn't.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean isDeveloperMode() {
        boolean isDevMode = PLATFORM.isDevelopmentEnvironment();
        if (isDevMode) {
            BaseLogger.log(BaseLogLevel.INFO, "Running in Developer mode.", true);
        } else {
            BaseLogger.log(BaseLogLevel.INFO, "Running in Production mode.", true);
        }
        return isDevMode;
    }
}
