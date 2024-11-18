// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.logging;

import java.util.logging.Level;

/**
 * A {@code public Interface} for providing color codes based on log levels.
 * <p>
 * This interface defines a method to retrieve color codes for various log levels. Implementations should provide
 * the appropriate color codes for each log level to enhance the readability of log messages.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getColor(Level)} - Retrieves the color code associated with a specific {@link Level} of logging.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ILogColorProvider {

    /**
     * A {@link String} that provides the color code for the specified {@link Level}.
     * <p>
     * Implementations should return a color code suitable for displaying log messages with the given log level.
     * </p>
     *
     * @param pLevel {@link Level} - The log level for which to retrieve the color code.
     * @return The color code as a {@link String} for the specified log level.
     * @author MeAlam
     * @since 1.0.0
     */
    String getColor(Level pLevel);
}
