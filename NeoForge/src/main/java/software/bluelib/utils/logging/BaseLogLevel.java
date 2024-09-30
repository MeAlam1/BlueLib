// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Level;

/**
 * A {@code class} defining custom log levels for the BlueLib logging system.
 * <p>
 * This class extends the standard {@link Level} class to introduce additional log levels
 * with specific names and integer values:
 * <ul>
 *   <li>{@link #INFO} - Standard informational log level.</li>
 *   <li>{@link #ERROR} - Log level for error messages.</li>
 *   <li>{@link #WARNING} - Log level for warning messages.</li>
 *   <li>{@link #SUCCESS} - Custom log level for indicating successful operations.</li>
 *   <li>{@link #BLUELIB} - Custom log level specific to BlueLib.</li>
 * </ul>
 * @author MeAlam
 * @since 1.0.0
 */
public class BaseLogLevel {
    public static final Level INFO = new Level("INFO", Level.INFO.intValue()) {};
    public static final Level ERROR = new Level("ERROR", Level.SEVERE.intValue()) {};
    public static final Level WARNING = new Level("WARNING", Level.WARNING.intValue()) {};

    public static final Level SUCCESS = new Level("SUCCESS", Level.INFO.intValue() + 50) {};
    public static final Level BLUELIB = new Level("BlueLib Developer", Level.INFO.intValue() + 50) {};
}
