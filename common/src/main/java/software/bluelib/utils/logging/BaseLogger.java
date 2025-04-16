// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Level;
import software.bluelib.BlueLibConstants;

@SuppressWarnings("unused")
public class BaseLogger {

    private BaseLogger() {}

    public static void setBlueLibLoggingEnabled(boolean pEnabled) {
        BlueLibConstants.isBlueLibLoggingEnabled = pEnabled;
    }

    public static boolean isBlueLibLoggingEnabled() {
        return BlueLibConstants.isBlueLibLoggingEnabled;
    }

    public static boolean isLoggingEnabled() {
        return BlueLibConstants.isLoggingEnabled;
    }

    public static void setLoggingEnabled(boolean pEnabled) {
        BlueLibConstants.isLoggingEnabled = pEnabled;
    }

    static {
        LoggerConfig.configureLogger(BlueLibConstants.LOGGER, new DefaultLogColorProvider());
    }

    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable, boolean pIsBlueLib) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                pIsBlueLib && BlueLibConstants.isBlueLibLoggingEnabled ||
                !pIsBlueLib && BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel, String pMessage, boolean pIsBlueLib) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                pIsBlueLib && BlueLibConstants.isBlueLibLoggingEnabled ||
                !pIsBlueLib && BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage);
        }
    }

    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel, String pMessage) {
        if (pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                BlueLibConstants.isLoggingEnabled) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage);
        }
    }

    public static void logBlueLib(String pMessage) {
        BlueLibConstants.LOGGER.log(BaseLogLevel.BLUELIB, pMessage);
    }
}
