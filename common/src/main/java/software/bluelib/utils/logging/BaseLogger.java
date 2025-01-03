// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Level;
import software.bluelib.BlueLibConstants;

/**
 * A {@code public class} responsible for logging messages
 * with various logging levels and configurations for {@code BlueLib}.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #setBlueLibLoggingEnabled(boolean)} - Enables or disables {@code BlueLib} specific logging.</li>
 * <li>{@link #isBlueLibLoggingEnabled()} - Checks if {@code BlueLib} logging is enabled.</li>
 * <li>{@link #log(Level, String, Throwable, boolean)} - Logs a message with an associated {@link Throwable}.</li>
 * <li>{@link #log(Level, String, boolean)} - Logs a message with a specified logging level.</li>
 * <li>{@link #log(Level, String, Throwable)} - Logs a message with an associated {@link Throwable}, if logging is enabled.</li>
 * <li>{@link #log(Level, String)} - Logs a message with a specified logging level, if logging is enabled.</li>
 * <li>{@link #logBlueLib(String)} - Logs a {@code BlueLib} specific message.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 * @version 1.7.0
 */
@SuppressWarnings("unused")
public class BaseLogger {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private BaseLogger() {}

    /**
     * A {@code void} to enable or disable {@code BlueLib} specific logging.
     *
     * @param pEnabled {@link Boolean} - Indicates whether to enable or disable BlueLib logging.
     * @since 1.0.0
     */
    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        BlueLibConstants.isBlueLibLoggingEnabled = pEnabled;
    }

    /**
     * A {@link Boolean} method that checks if BlueLib logging is enabled.
     *
     * @return {@code true} if BlueLib logging is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean isBlueLibLoggingEnabled() {
        return BlueLibConstants.isBlueLibLoggingEnabled;
    }

    /**
     * A {@link Boolean} method that checks if logging is enabled.
     *
     * @return {@code true} if general logging is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean isLoggingEnabled() {
        return BlueLibConstants.isLoggingEnabled;
    }

    /**
     * A {@code void} to enable or disable general logging.
     *
     * @param pEnabled {@link Boolean} - Indicates whether to enable or disable general logging.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void setLoggingEnabled(boolean pEnabled) {
        BlueLibConstants.isLoggingEnabled = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(BlueLibConstants.LOGGER, new DefaultLogColorProvider());
    }

    /**
     * A {@code public static void} that logs a message with an associated {@link Throwable}
     * if {@code BlueLib} logging is enabled.
     *
     * @param pLogLevel  {@link Level} - The logging level to use.
     * @param pMessage   {@link String} - The message to log.
     * @param pThrowable {@link Throwable} - The throwable to log with the message.
     * @param pIsBlueLib {@link Boolean} - Indicates if the message is {@code BlueLib} specific.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable, boolean pIsBlueLib) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                pIsBlueLib && BlueLibConstants.isBlueLibLoggingEnabled ||
                !pIsBlueLib && BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage, pThrowable);
        }
    }

    /**
     * A {@code public static void} that logs a message if {@code BlueLib} logging is enabled.
     *
     * @param pLogLevel  {@link Level} - The logging level to use.
     * @param pMessage   {@link String} - The message to log.
     * @param pIsBlueLib {@link Boolean} - Indicates if the message is {@code BlueLib} specific.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, boolean pIsBlueLib) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                pIsBlueLib && BlueLibConstants.isBlueLibLoggingEnabled ||
                !pIsBlueLib && BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage);
        }
    }

    /**
     * A {@code public static void} that logs a message with an associated {@link Throwable}
     * if general logging is enabled.
     *
     * @param pLogLevel  {@link Level} - The logging level to use.
     * @param pMessage   {@link String} - The message to log.
     * @param pThrowable {@link Throwable} - The throwable to log with the message.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage, pThrowable);
        }
    }

    /**
     * A {@code public static void} that logs a message if general logging is enabled.
     *
     * @param pLogLevel {@link Level} - The logging level to use.
     * @param pMessage  {@link String} - The message to log.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void log(Level pLogLevel, String pMessage) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage);
        }
    }

    /**
     * A {@code public static void} that logs a {@code BlueLib} specific message at the {@code BlueLib} log level.
     *
     * @param pMessage {@link String} - The {@code BlueLib} message to log.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void logBlueLib(String pMessage) {
        BlueLibConstants.LOGGER.log(BaseLogLevel.BLUELIB, pMessage);
    }
}
