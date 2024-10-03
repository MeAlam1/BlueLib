// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Level;
import java.util.logging.Logger;
import software.bluelib.BlueLib;

/**
 * A {@code public class} responsible for logging messages
 * with various logging levels and configurations for {@link BlueLib}.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #setBlueLibLoggingEnabled(boolean)} - Enables or disables {@link BlueLib} specific logging.</li>
 *   <li>{@link #isBlueLibLoggingEnabled()} - Checks if {@link BlueLib} logging is enabled.</li>
 *   <li>{@link #log(Level, String, Throwable, boolean)} - Logs a message with an associated {@link Throwable}.</li>
 *   <li>{@link #log(Level, String, boolean)} - Logs a message with a specified logging level.</li>
 *   <li>{@link #log(Level, String, Throwable)} - Logs a message with an associated {@link Throwable}, if logging is enabled.</li>
 *   <li>{@link #log(Level, String)} - Logs a message with a specified logging level, if logging is enabled.</li>
 *   <li>{@link #logBlueLib(String)} - Logs a {@link BlueLib} specific message.</li>
 * </ul>
 *
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class BaseLogger {

    /**
     * A {@link Logger} instance for logging messages.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    private static final Logger logger = Logger.getLogger(BaseLogger.class.getName());

    //FIXME: Set to false before release
    /**
     * A {@link Boolean} to enable or disable BlueLib specific logging.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    private static boolean bluelibLogging = true;

    /**
     * A {@link Boolean} to enable or disable general logging.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    private static boolean isLoggingEnabled = true;

    /**
     * A {@code void} to enable or disable {@link BlueLib} specific logging.
     *
     * @param pEnabled {@link boolean} - Indicates whether to enable or disable BlueLib logging.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        bluelibLogging = pEnabled;
    }

    /**
     * A {@link Boolean} method that checks if BlueLib logging is enabled.
     *
     * @return {@code true} if BlueLib logging is enabled, {@code false} otherwise.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static boolean isBlueLibLoggingEnabled() {
        return bluelibLogging;
    }

    /**
     * A {@link Boolean} method that checks if logging is enabled.
     *
     * @return {@code true} if general logging is enabled, {@code false} otherwise.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    /**
     * A {@code void} to enable or disable general logging.
     *
     * @param pEnabled {@link boolean} - Indicates whether to enable or disable general logging.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void setLoggingEnabled(boolean pEnabled) {
        isLoggingEnabled = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(logger, new DefaultLogColorProvider());
    }

    /**
     * A {@code void} that logs a message with an associated {@link Throwable} if {@link BlueLib} logging is enabled.
     *
     * @param pLogLevel {@link Level} - The logging level to use.
     * @param pMessage {@link String} - The message to log.
     * @param pThrowable {@link Throwable} - The throwable to log with the message.
     * @param pIsBlueLib {@link boolean} - Indicates if the message is {@link BlueLib} specific.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable, boolean pIsBlueLib) {
        if (pIsBlueLib && bluelibLogging) {
            logger.log(pLogLevel, pMessage, pThrowable);
        }
    }

    /**
     * A {@code void} that logs a message if BlueLib logging is enabled.
     *
     * @param pLogLevel {@link Level} - The logging level to use.
     * @param pMessage {@link String} - The message to log.
     * @param pIsBlueLib {@link boolean} - Indicates if the message is {@link BlueLib} specific.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, boolean pIsBlueLib) {
        if (pIsBlueLib && bluelibLogging) {
            logger.log(pLogLevel, pMessage);
        }
    }

    /**
     * A {@code void} that logs a message with an associated {@link Throwable} if general logging is enabled.
     *
     * @param pLogLevel {@link Level} - The logging level to use.
     * @param pMessage {@link String} - The message to log.
     * @param pThrowable {@link Throwable} - The throwable to log with the message.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable) {
        if (isLoggingEnabled) {
            logger.log(pLogLevel, pMessage, pThrowable);
        }
    }

    /**
     * A {@code void} that logs a message if general logging is enabled.
     *
     * @param pLogLevel {@link Level} - The logging level to use.
     * @param pMessage {@link String} - The message to log.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage) {
        if (isLoggingEnabled) {
            logger.log(pLogLevel, pMessage);
        }
    }

    /**
     * A {@code void} that logs a {@link BlueLib} specific message at the {@link BlueLib} log level.
     *
     * @param pMessage {@link String} - The {@link BlueLib} message to log.
     * @Co-author MeAlam
     * @since 1.0.0
     */
    public static void logBlueLib(String pMessage) {
        logger.log(BaseLogLevel.BLUELIB, pMessage);
    }
}
