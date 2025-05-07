// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.logging;

import java.util.logging.Level;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.minecraft.ClientUtils;


@SuppressWarnings("unused")
public class BaseLogger {

    private BaseLogger() {}

    static {
        LoggerConfig.configureLogger(BlueLibConstants.LOGGER, new DefaultLogColorProvider());
    }

    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable, boolean pIsBlueLib) {
        if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
            logBoth(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel, String pMessage, boolean pIsBlueLib) {
        if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
            logBoth(pLogLevel, pMessage);
        }
    }

    public static void log(Level pLogLevel, String pMessage, Throwable pThrowable) {
        if (shouldLog(pLogLevel)) {
            logBoth(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel, String pMessage) {
        if (shouldLog(pLogLevel)) {
            logBoth(pLogLevel, pMessage);
        }
    }

    public static void logBlueLib(String pMessage) {
        BlueLibConstants.LOGGER.log(BaseLogLevel.BLUELIB, pMessage);
    }
    
    private static boolean shouldLogBlueLib(Level pLogLevel, boolean pIsBlueLib) {
        return pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                !ClientUtils.isInWorld() ||
                pIsBlueLib && software.bluelib.config.LoggerConfig.isBlueLibLoggingEnabled ||
                !pIsBlueLib && software.bluelib.config.LoggerConfig.isLoggingEnabled;
    }
    
    private static boolean shouldLog(Level pLogLevel) {
        return pLogLevel == BaseLogLevel.ERROR ||
                pLogLevel == BaseLogLevel.WARNING ||
                pLogLevel == BaseLogLevel.BLUELIB ||
                !ClientUtils.isInWorld() ||
                software.bluelib.config.LoggerConfig.isLoggingEnabled;
    }
    
    private static void logBoth(Level pLogLevel, String pMessage) {
            BlueLibConstants.LOGGER.log(pLogLevel, pMessage);
            sendToAdmins(pMessage, null);
    }

    private static void logBoth(Level pLogLevel, String pMessage, Throwable pThrowable) {
        BlueLibConstants.LOGGER.log(pLogLevel, pMessage, pThrowable);
        sendToAdmins(pMessage, pThrowable);
    }

    private static void sendToAdmins(String pMessage, Throwable pThrowable) {
    }
}
