// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseLogger {

    private static final Logger logger = Logger.getLogger(BaseLogger.class.getName());

    //FIXME: Set to false before release
    private static boolean bluelibLogging = true;

    private static boolean isLoggingEnabled = true;

    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        bluelibLogging = pEnabled;
    }

    public static boolean isBlueLibLoggingEnabled() {
        return bluelibLogging;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void setLoggingEnabled(boolean pEnabled) {
        isLoggingEnabled = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(logger, new DefaultLogColorProvider());
    }

    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable, boolean pIsBlueLib) {
        if (pIsBlueLib && bluelibLogging) {
            logger.log(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel,String pMessage, boolean pIsBlueLib) {
        if (pIsBlueLib && bluelibLogging) {
            logger.log(pLogLevel, pMessage);
        }
    }

    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable) {
        if (isLoggingEnabled) {
            logger.log(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel,String pMessage) {
        if (isLoggingEnabled) {
            logger.log(pLogLevel, pMessage);
        }
    }

    public static void logBlueLib(String pMessage) {
        logger.log(BaseLogLevel.BLUELIB, pMessage);
    }
}
