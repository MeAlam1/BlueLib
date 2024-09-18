package software.bluelib.utils.logging;

import java.util.logging.Logger;

public class BaseLogger {

    private static final Logger logger = Logger.getLogger(BaseLogger.class.getName());

    //FIXME: Set to false before release
    private static boolean bluelibLogging = true;

    private static boolean logging = true;

    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        bluelibLogging = pEnabled;
    }

    public static boolean isBlueLibLoggingEnabled() {
        return bluelibLogging;
    }

    public static boolean isLoggingEnabled() {
        return logging;
    }

    public static void setLoggingEnabled(boolean pEnabled) {
        logging = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(logger, new DefaultLogColorProvider());
    }

    public static void logError(String pMessage, Throwable pThrowable) {
        logger.log(BlueLibLogLevel.ERROR, pMessage, pThrowable);
    }

    public static void logWarning(String pMessage) {
        logger.log(BlueLibLogLevel.WARNING, pMessage);
    }

    public static void logBlueLib(String pMessage) {
        logger.log(BlueLibLogLevel.BLUELIB, pMessage);
    }

    public static void bluelibLogSuccess(String pMessage) {
        if (bluelibLogging) {
            logger.log(BlueLibLogLevel.SUCCESS, pMessage);
        }
    }

    public static void bluelibLogInfo(String pMessage) {
        if (bluelibLogging) {
            logger.log(BlueLibLogLevel.INFO, pMessage);
        }
    }

    public static void logSuccess(String pMessage) {
        if (logging) {
            logger.log(BlueLibLogLevel.SUCCESS, pMessage);
        }
    }

    public static void logInfo(String pMessage) {
        if (logging) {
            logger.log(BlueLibLogLevel.INFO, pMessage);
        }
    }
}
