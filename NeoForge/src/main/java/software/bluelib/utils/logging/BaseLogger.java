// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Logger;

/**
 * A {@code class} class that provides a centralized logging mechanism for BlueLib,
 * with support for custom log levels and colored output.
 * <p>
 * The {@link BaseLogger} allows for enabling/disabling logging and includes methods
 * for logging messages at different levels. It also supports conditional logging based
 * on the {@code bluelibLogging} flag, which determines if BlueLib-specific logs should be output.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #setBlueLibLoggingEnabled(boolean)} - Enables or disables BlueLib-specific logging.</li>
 *   <li>{@link #isBlueLibLoggingEnabled()} - Checks if BlueLib-specific logging is enabled.</li>
 *   <li>{@link #isLoggingEnabled()} - Checks if general logging is enabled.</li>
 *   <li>{@link #setLoggingEnabled(boolean)} - Enables or disables general logging.</li>
 *   <li>{@link #logError(String, Throwable)} - Logs an error message with an associated throwable.</li>
 *   <li>{@link #logWarning(String)} - Logs a warning message.</li>
 *   <li>{@link #logBlueLib(String)} - Logs a BlueLib-specific message.</li>
 *   <li>{@link #bluelibLogSuccess(String)} - Logs a success message if BlueLib-specific logging is enabled.</li>
 *   <li>{@link #bluelibLogInfo(String)} - Logs an info message if BlueLib-specific logging is enabled.</li>
 *   <li>{@link #logSuccess(String)} - Logs a success message if general logging is enabled.</li>
 *   <li>{@link #logInfo(String)} - Logs an info message if general logging is enabled.</li>
 * </ul>
 * @author MeAlam
 * @since 1.0.0
 */
public class BaseLogger {

    private static final Logger logger = Logger.getLogger(BaseLogger.class.getName());

    //FIXME: Set to false before release
    private static boolean bluelibLogging = false;

    private static boolean logging = true;

    /**
     * Enables or disables BlueLib-specific logging.
     *
     * @param pEnabled {@link boolean} - {@code true} to enable BlueLib logging, {@code false} to disable.
     */
    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        bluelibLogging = pEnabled;
    }

    /**
     * Checks if BlueLib-specific logging is enabled.
     *
     * @return {@link boolean} - {@code true} if BlueLib logging is enabled, {@code false} otherwise.
     */
    public static boolean isBlueLibLoggingEnabled() {
        return bluelibLogging;
    }

    /**
     * Checks if general logging is enabled.
     *
     * @return {@link boolean} - {@code true} if general logging is enabled, {@code false} otherwise.
     */
    public static boolean isLoggingEnabled() {
        return logging;
    }

    /**
     * Enables or disables general logging.
     *
     * @param pEnabled {@link boolean} - {@code true} to enable logging, {@code false} to disable.
     */
    public static void setLoggingEnabled(boolean pEnabled) {
        logging = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(logger, new DefaultLogColorProvider());
    }

    /**
     * A {@code void} method that logs an error message with an associated {@link Throwable}.
     *
     * @param pMessage {@link String} - The error message to be logged.
     * @param pThrowable {@link Throwable} - The throwable associated with the error.
     */
    public static void logError(String pMessage, Throwable pThrowable) {
        logger.log(BlueLibLogLevel.ERROR, pMessage, pThrowable);
    }

    /**
     * A {@code void} method that logs a warning message.
     *
     * @param pMessage {@link String} - The warning message to be logged.
     */
    public static void logWarning(String pMessage) {
        logger.log(BlueLibLogLevel.WARNING, pMessage);
    }

    /**
     * A {@code void} method that logs a BlueLib-specific message.
     *
     * @param pMessage {@link String} - The BlueLib-specific message to be logged.
     */
    public static void logBlueLib(String pMessage) {
        logger.log(BlueLibLogLevel.BLUELIB, pMessage);
    }

    /**
     * A {@code void} method that logs a success message if BlueLib-specific logging is enabled.
     *
     * @param pMessage {@link String} - The success message to be logged.
     */
    public static void bluelibLogSuccess(String pMessage) {
        if (bluelibLogging) {
            logger.log(BlueLibLogLevel.SUCCESS, pMessage);
        }
    }

    /**
     * A {@code void} method that logs an info message if BlueLib-specific logging is enabled.
     *
     * @param pMessage {@link String} - The info message to be logged.
     */
    public static void bluelibLogInfo(String pMessage) {
        if (bluelibLogging) {
            logger.log(BlueLibLogLevel.INFO, pMessage);
        }
    }

    /**
     * A {@code void} method that logs a success message if general logging is enabled.
     *
     * @param pMessage {@link String} - The success message to be logged.
     */
    public static void logSuccess(String pMessage) {
        if (logging) {
            logger.log(BlueLibLogLevel.SUCCESS, pMessage);
        }
    }

    /**
     * A {@code void} method that logs an info message if general logging is enabled.
     *
     * @param pMessage {@link String} - The info message to be logged.
     */
    public static void logInfo(String pMessage) {
        if (logging) {
            logger.log(BlueLibLogLevel.INFO, pMessage);
        }
    }
}
