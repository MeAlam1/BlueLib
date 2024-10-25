// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

/**
 * A {@code public class} that defines common constants used across the BlueLib mod.
 * <p>
 * This class contains constants such as the mod's {@link #MOD_ID}, {@link #MOD_NAME}, and a
 * {@link ScheduledExecutorService} for scheduling tasks.
 * </p>
 * <p>
 * Key Fields:
 * <ul>
 *   <li>{@link #SCHEDULER} - Executor for scheduling delayed tasks.</li>
 *   <li>{@link #MOD_ID} - Unique identifier for the mod.</li>
 *   <li>{@link #MOD_NAME} - Display name of the mod.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class BlueLibConstants {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private BlueLibConstants() {
    }

    /**
     * A {@link Logger} instance for logging messages.
     *
     * @since 1.0.0
     */
    public static final Logger LOGGER = Logger.getLogger(BlueLibConstants.MOD_NAME);

    /**
     * A {@code public static} {@link ScheduledExecutorService} used to schedule tasks, such as printing messages after a delay.
     * <p>
     * This executor runs tasks on a single thread to ensure delayed tasks run in a separate thread from the main thread.
     * </p>
     *
     * @since 1.0.0
     */
    public static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    /**
     * A {@code public static final} {@link String} representing the Mod ID for the {@code BlueLib} mod.
     * <p>This serves as a unique identifier for the mod.</p>
     *
     * @since 1.0.0
     */
    public static final String MOD_ID = "bluelib";

    /**
     * A {@code public static final} {@link String} representing the Mod Name for the {@code BlueLib} mod.
     *
     * @since 1.0.0
     */
    public static final String MOD_NAME = "BlueLib";

    /** TODO: Always have on False when pushing to production
     * A {@code public static final} {@link Boolean} indicating whether the example features should be enabled. <br>
     * Should always be false in production.
     *
     * @since 1.0.0
     */
    public static final Boolean isExampleEnabled = true;

    /** TODO: Always have on False when pushing to production
     * A {@link Boolean} to enable or disable BlueLib specific logging.
     *
     * @since 1.0.0
     */
    public static boolean isBlueLibLoggingEnabled = true;

    /** TODO: Always have on False when pushing to production
     * A {@link Boolean} to enable or disable general logging.
     *
     * @since 1.0.0
     */
    public static boolean isLoggingEnabled = true;
}