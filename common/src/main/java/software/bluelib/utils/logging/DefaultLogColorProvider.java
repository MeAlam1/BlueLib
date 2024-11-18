// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import software.bluelib.interfaces.logging.ILogColorProvider;

import java.util.logging.Level;

/**
 * A {@code class} that implements the {@link ILogColorProvider} interface
 * to provide color codes for different log levels.
 * <p>
 * This implementation uses predefined colors for various log levels, including:
 * <ul>
 *   <li>{@link BaseLogLevel#ERROR} - Red color.</li>
 *   <li>{@link BaseLogLevel#WARNING} - Orange color.</li>
 *   <li>{@link BaseLogLevel#INFO} - Blue color.</li>
 *   <li>{@link BaseLogLevel#SUCCESS} - Green color.</li>
 *   <li>{@link BaseLogLevel#BLUELIB} - Green color.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class DefaultLogColorProvider implements ILogColorProvider {

    /**
     * A {@link String} that provides the color code for a given {@link Level}.
     * The color code is determined based on the log level.
     *
     * @param pLevel {@link Level} - The log level for which to determine the color.
     * @return The color code associated with the {@code pLevel}.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public String getColor(Level pLevel) {
        if (pLevel == BaseLogLevel.ERROR) {
            return LoggerConfig.RED;
        } else if (pLevel == BaseLogLevel.WARNING) {
            return LoggerConfig.ORANGE;
        } else if (pLevel == BaseLogLevel.INFO) {
            return LoggerConfig.BLUE;
        } else if (pLevel == BaseLogLevel.SUCCESS) {
            return LoggerConfig.GREEN;
        } else if (pLevel == BaseLogLevel.BLUELIB) {
            return LoggerConfig.GREEN;
        } else {
            return LoggerConfig.RESET;
        }
    }
}
