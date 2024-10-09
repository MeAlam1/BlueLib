// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import software.bluelib.BlueLibConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@code public class} responsible for logging messages
 * with various logging levels and configurations for {@code BlueLib}.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #setBlueLibLoggingEnabled(boolean)} - Enables or disables {@code BlueLib} specific logging.</li>
 *   <li>{@link #isBlueLibLoggingEnabled()} - Checks if {@code BlueLib} logging is enabled.</li>
 *   <li>{@link #log(Level, String, Throwable, boolean)} - Logs a message with an associated {@link Throwable}.</li>
 *   <li>{@link #log(Level, String, boolean)} - Logs a message with a specified logging level.</li>
 *   <li>{@link #log(Level, String, Throwable)} - Logs a message with an associated {@link Throwable}, if logging is enabled.</li>
 *   <li>{@link #log(Level, String)} - Logs a message with a specified logging level, if logging is enabled.</li>
 *   <li>{@link #logBlueLib(String)} - Logs a {@code BlueLib} specific message.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class BaseLogger {

    /**
     * A {@link Logger} instance for logging messages.
     *
     * @since 1.0.0
     */
    private static final Logger logger = Logger.getLogger(BlueLibConstants.MOD_NAME);

    /**
     * A {@code void} to enable or disable {@code BlueLib} specific logging.
     *
     * @param pEnabled {@link boolean} - Indicates whether to enable or disable BlueLib logging.
     * @since 1.0.0
     */
    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        BlueLibConstants.isBlueLibLoggingEnabled = pEnabled;
    }

    /**
     * A {@link Boolean} method that checks if BlueLib logging is enabled.
     *
     * @return {@code true} if BlueLib logging is enabled, {@code false} otherwise.
     * @since 1.0.0
     */
    public static boolean isBlueLibLoggingEnabled() {
        return BlueLibConstants.isBlueLibLoggingEnabled;
    }

    /**
     * A {@link Boolean} method that checks if logging is enabled.
     *
     * @return {@code true} if general logging is enabled, {@code false} otherwise.
     * @since 1.0.0
     */
    public static boolean isLoggingEnabled() {
        return BlueLibConstants.isLoggingEnabled;
    }

    /**
     * A {@code void} to enable or disable general logging.
     *
     * @param pEnabled {@link boolean} - Indicates whether to enable or disable general logging.
     * @since 1.0.0
     */
    public static void setLoggingEnabled(boolean pEnabled) {
        BlueLibConstants.isLoggingEnabled = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(logger, new DefaultLogColorProvider());
    }

    /**
     * A {@code public static void} that logs a message with an associated {@link Throwable}
     * if {@code BlueLib} logging is enabled.
     *
     * @param pLogLevel  {@link Level} - The logging level to use.
     * @param pMessage   {@link String} - The message to log.
     * @param pThrowable {@link Throwable} - The throwable to log with the message.
     * @param pIsBlueLib {@link boolean} - Indicates if the message is {@code BlueLib} specific.
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable, boolean pIsBlueLib) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                pIsBlueLib && BlueLibConstants.isBlueLibLoggingEnabled ||
                !pIsBlueLib && BlueLibConstants.isLoggingEnabled) {
            logger.log(pLogLevel, pMessage, pThrowable);
        }
    }

    /**
     * A {@code public static void} that logs a message if {@code BlueLib} logging is enabled.
     *
     * @param pLogLevel  {@link Level} - The logging level to use.
     * @param pMessage   {@link String} - The message to log.
     * @param pIsBlueLib {@link boolean} - Indicates if the message is {@code BlueLib} specific.
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, boolean pIsBlueLib) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                pIsBlueLib && BlueLibConstants.isBlueLibLoggingEnabled ||
                !pIsBlueLib && BlueLibConstants.isLoggingEnabled) {
            logger.log(pLogLevel, pMessage);
        }
    }

    /**
     * A {@code public static void} that logs a message with an associated {@link Throwable}
     * if general logging is enabled.
     *
     * @param pLogLevel  {@link Level} - The logging level to use.
     * @param pMessage   {@link String} - The message to log.
     * @param pThrowable {@link Throwable} - The throwable to log with the message.
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                BlueLibConstants.isLoggingEnabled) {
            logger.log(pLogLevel, pMessage, pThrowable);
        }
    }

    /**
     * A {@code public static void} that logs a message if general logging is enabled.
     *
     * @param pLogLevel {@link Level} - The logging level to use.
     * @param pMessage  {@link String} - The message to log.
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                BlueLibConstants.isLoggingEnabled) {
            logger.log(pLogLevel, pMessage);
        }
    }

    /**
     * A {@code public static void} that logs a {@code BlueLib} specific message at the {@code BlueLib} log level.
     *
     * @param pMessage {@link String} - The {@code BlueLib} message to log.
     * @since 1.0.0
     */
    public static void logBlueLib(String pMessage) {
        logger.log(BaseLogLevel.BLUELIB, pMessage);
    }
}
