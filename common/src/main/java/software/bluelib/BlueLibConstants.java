// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import java.util.concurrent.ScheduledExecutorService;

/**
 * A {@code public class} that defines common constants used across the BlueLib mod.
 * <p>
 * This class contains constants such as the mod's {@link #MOD_ID}, {@link #MOD_NAME}, and a
 * {@link ScheduledExecutorService} for scheduling tasks.
 * </p>
 * <p>
 * Key Fields:
 * <ul>
 * <li>{@link #MOD_ID} - Unique identifier for the mod.</li>
 * <li>{@link #MOD_NAME} - Display name of the mod.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @since 1.7.0
 */
public class BlueLibConstants {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this class.
     * </p>
     *
     * @author MeAlam
     * @since 1.7.0
     */
    private BlueLibConstants() {}

    /**
     * A {@code public static final} {@link String} representing the Mod ID for the {@code BlueLib} mod.
     * <p>This serves as a unique identifier for the mod.</p>
     *
     * @since 1.7.0
     */
    public static final String MOD_ID = "bluelib_examples";

    /**
     * A {@code public static final} {@link String} representing the Mod Name for the {@code BlueLib} mod.
     *
     * @since 1.7.0
     */
    public static final String MOD_NAME = "BlueLib";
}
