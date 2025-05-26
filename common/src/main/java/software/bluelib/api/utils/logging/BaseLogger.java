/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.logging;

import java.util.function.Supplier;
import java.util.logging.Level;
import net.minecraft.network.chat.Component;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.minecraft.ClientUtils;

@SuppressWarnings("unused")
public class BaseLogger {

    private BaseLogger() {}

    static {
        LoggerConfig.configureLogger(BlueLibConstants.LOGGER, new DefaultLogColorProvider());
    }

    // --- Supplier-based logging ---

    public static void log(boolean pIsBlueLib, Level pLogLevel, Supplier<String> messageSupplier, Throwable... pThrowable) {
        if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
            logBoth(pLogLevel, Component.literal(messageSupplier.get()), pThrowable);
        }
    }

    public static void log(boolean pIsBlueLib, Level pLogLevel, Supplier<String> messageSupplier) {
        if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
            logBoth(pLogLevel, Component.literal(messageSupplier.get()));
        }
    }

    public static void log(Level pLogLevel, Supplier<String> messageSupplier, Throwable... pThrowable) {
        if (shouldLog(pLogLevel)) {
            logBoth(pLogLevel, Component.literal(messageSupplier.get()), pThrowable);
        }
    }

    public static void log(Level pLogLevel, Supplier<String> messageSupplier) {
        if (shouldLog(pLogLevel)) {
            logBoth(pLogLevel, Component.literal(messageSupplier.get()));
        }
    }

    // --- Existing methods (String and Component overloads) ---

    public static void log(boolean pIsBlueLib, Level pLogLevel, Component pMessage, Throwable... pThrowable) {
        if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
            logBoth(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(boolean pIsBlueLib, Level pLogLevel, Component pMessage) {
        if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
            logBoth(pLogLevel, pMessage);
        }
    }

    public static void log(Level pLogLevel, Component pMessage, Throwable... pThrowable) {
        if (shouldLog(pLogLevel)) {
            logBoth(pLogLevel, pMessage, pThrowable);
        }
    }

    public static void log(Level pLogLevel, Component pMessage) {
        if (shouldLog(pLogLevel)) {
            logBoth(pLogLevel, pMessage);
        }
    }

    public static void log(boolean pIsBlueLib, Level pLogLevel, String message, Throwable... pThrowable) {
        log(pIsBlueLib, pLogLevel, Component.literal(message), pThrowable);
    }

    public static void log(boolean pIsBlueLib, Level pLogLevel, String message) {
        log(pIsBlueLib, pLogLevel, Component.literal(message));
    }

    public static void log(Level pLogLevel, String message, Throwable... pThrowable) {
        log(pLogLevel, Component.literal(message), pThrowable);
    }

    public static void log(Level pLogLevel, String message) {
        log(pLogLevel, Component.literal(message));
    }

    public static void logBlueLib(Component pMessage) {
        String translatedMessage = pMessage.getString();
        BlueLibConstants.LOGGER.log(BaseLogLevel.BLUELIB, translatedMessage);
    }

    // --- Internal helpers ---

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

    private static void logBoth(Level pLogLevel, Component pMessage) {
        String translatedMessage = pMessage.getString();
        BlueLibConstants.LOGGER.log(pLogLevel, translatedMessage);
    }

    private static void logBoth(Level pLogLevel, Component pMessage, Throwable... pThrowable) {
        String translatedMessage = pMessage.getString();
        BlueLibConstants.LOGGER.log(pLogLevel, translatedMessage, pThrowable);
    }
}
