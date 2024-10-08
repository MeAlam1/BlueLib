// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import software.bluelib.BlueLibConstants;
import software.bluelib.annotations.EnableLogging;

import java.lang.reflect.Method;
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

    //FIXME: Set to false before release
    /**
     * A {@link Boolean} to enable or disable BlueLib specific logging.
     *
     * @since 1.0.0
     */
    private static boolean bluelibLogging = false;

    /**
     * A {@link Boolean} to enable or disable general logging.
     *
     * @since 1.0.0
     */
    private static boolean isLoggingEnabled = false;

    /**
     * A {@code void} to enable or disable {@code BlueLib} specific logging.
     *
     * @param pEnabled {@link boolean} - Indicates whether to enable or disable BlueLib logging.
     * @since 1.0.0
     */
    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        bluelibLogging = pEnabled;
    }

    /**
     * A {@link Boolean} method that checks if BlueLib logging is enabled.
     *
     * @return {@code true} if BlueLib logging is enabled, {@code false} otherwise.
     * @since 1.0.0
     */
    public static boolean isBlueLibLoggingEnabled() {
        return bluelibLogging;
    }

    /**
     * A {@link Boolean} method that checks if logging is enabled.
     *
     * @return {@code true} if general logging is enabled, {@code false} otherwise.
     * @since 1.0.0
     */
    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    /**
     * A {@code void} to enable or disable general logging.
     *
     * @param pEnabled {@link boolean} - Indicates whether to enable or disable general logging.
     * @since 1.0.0
     */
    public static void setLoggingEnabled(boolean pEnabled) {
        isLoggingEnabled = pEnabled;
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
        try {
            if (pLogLevel == BaseLogLevel.ERROR || pLogLevel == BaseLogLevel.WARNING || pLogLevel == BaseLogLevel.BLUELIB) {
                logger.log(pLogLevel, pMessage, pThrowable);
            } else if (pIsBlueLib && bluelibLogging) {
                logger.log(pLogLevel, pMessage, pThrowable);
            } else if (!pIsBlueLib && isLoggingEnabled) {
                logger.log(pLogLevel, pMessage, pThrowable);
            }
        } catch (Exception pException) {
            logger.log(Level.SEVERE, "Failed to log message.", pException);
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
        try {
            if (pLogLevel == BaseLogLevel.ERROR || pLogLevel == BaseLogLevel.WARNING || pLogLevel == BaseLogLevel.BLUELIB) {
                logger.log(pLogLevel, pMessage);
            } else if (pIsBlueLib && bluelibLogging) {
                logger.log(pLogLevel, pMessage);
            } else if (!pIsBlueLib && isLoggingEnabled) {
                logger.log(pLogLevel, pMessage);
            }
        } catch (Exception pException) {
            logger.log(Level.SEVERE, "Failed to log message.", pException);
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
        try {
            if (pLogLevel == BaseLogLevel.ERROR || pLogLevel == BaseLogLevel.WARNING || pLogLevel == BaseLogLevel.BLUELIB) {
                logger.log(pLogLevel, pMessage, pThrowable);
            } else if (isLoggingEnabled) {
                logger.log(pLogLevel, pMessage, pThrowable);
            } else {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                if (stackTrace.length > 3) {
                    Class<?> callingClass = Class.forName(stackTrace[3].getClassName());
                    Method callingMethod = callingClass.getMethod(stackTrace[3].getMethodName());

                    if (callingClass.isAnnotationPresent(EnableLogging.class)) {
                        logger.log(pLogLevel, pMessage, pThrowable);
                    } else if (callingMethod.isAnnotationPresent(EnableLogging.class)) {
                        logger.log(pLogLevel, pMessage, pThrowable);
                    }
                }
            }
        } catch (Exception pException) {
            logger.log(Level.SEVERE, "Failed to log message.", pException);
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
        try {
            if (pLogLevel == BaseLogLevel.ERROR || pLogLevel == BaseLogLevel.WARNING || pLogLevel == BaseLogLevel.BLUELIB) {
                logger.log(pLogLevel, pMessage);
            } else if (isLoggingEnabled) {
                logger.log(pLogLevel, pMessage);
            } else {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                if (stackTrace.length > 3) {
                    Class<?> callingClass = Class.forName(stackTrace[3].getClassName());
                    Method callingMethod = callingClass.getMethod(stackTrace[3].getMethodName());

                    if (callingClass.isAnnotationPresent(EnableLogging.class)) {
                        logger.log(pLogLevel, pMessage);
                    } else if (callingMethod.isAnnotationPresent(EnableLogging.class)) {
                        logger.log(pLogLevel, pMessage);
                    }
                }
            }
        } catch (Exception pException) {
            logger.log(Level.SEVERE, "Failed to log message.", pException);
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
