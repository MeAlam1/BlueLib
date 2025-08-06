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
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;

@SuppressWarnings("unused")
public class BaseLogger {

	private BaseLogger() {}

	static {
		LoggerConfig.configureLogger(BlueLibConstants.LOGGER, new DefaultLogColorProvider());
	}

	// --- Supplier-based logging ---

	public static void log(@NotNull Boolean pIsBlueLib, @NotNull Level pLogLevel, @NotNull Supplier<String> pMessageSupplier, @NotNull Throwable... pThrowable) {
		if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
			logBoth(pLogLevel, Component.literal(pMessageSupplier.get()), pThrowable);
		}
	}

	public static void log(@NotNull Boolean pIsBlueLib, @NotNull Level pLogLevel, @NotNull Supplier<String> pMessageSupplier) {
		if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
			logBoth(pLogLevel, Component.literal(pMessageSupplier.get()));
		}
	}

	public static void log(@NotNull Level pLogLevel, @NotNull Supplier<String> pMessageSupplier, @NotNull Throwable... pThrowable) {
		if (shouldLog(pLogLevel)) {
			logBoth(pLogLevel, Component.literal(pMessageSupplier.get()), pThrowable);
		}
	}

	public static void log(@NotNull Level pLogLevel, @NotNull Supplier<String> pMessageSupplier) {
		if (shouldLog(pLogLevel)) {
			logBoth(pLogLevel, Component.literal(pMessageSupplier.get()));
		}
	}

	// --- Existing methods (String and Component overloads) ---

	public static void log(@NotNull Boolean pIsBlueLib, @NotNull Level pLogLevel, @NotNull Component pMessage, @NotNull Throwable... pThrowable) {
		if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
			logBoth(pLogLevel, pMessage, pThrowable);
		}
	}

	public static void log(@NotNull Boolean pIsBlueLib, @NotNull Level pLogLevel, @NotNull Component pMessage) {
		if (shouldLogBlueLib(pLogLevel, pIsBlueLib)) {
			logBoth(pLogLevel, pMessage);
		}
	}

	public static void log(@NotNull Level pLogLevel, @NotNull Component pMessage, @NotNull Throwable... pThrowable) {
		if (shouldLog(pLogLevel)) {
			logBoth(pLogLevel, pMessage, pThrowable);
		}
	}

	public static void log(@NotNull Level pLogLevel, @NotNull Component pMessage) {
		if (shouldLog(pLogLevel)) {
			logBoth(pLogLevel, pMessage);
		}
	}

	public static void log(@NotNull Boolean pIsBlueLib, @NotNull Level pLogLevel, @NotNull String pMessage, @NotNull Throwable... pThrowable) {
		log(pIsBlueLib, pLogLevel, Component.literal(pMessage), pThrowable);
	}

	public static void log(@NotNull Boolean pIsBlueLib, @NotNull Level pLogLevel, @NotNull String pMessage) {
		log(pIsBlueLib, pLogLevel, Component.literal(pMessage));
	}

	public static void log(@NotNull Level pLogLevel, @NotNull String pMessage, @NotNull Throwable... pThrowable) {
		log(pLogLevel, Component.literal(pMessage), pThrowable);
	}

	public static void log(@NotNull Level pLogLevel, @NotNull String pMessage) {
		log(pLogLevel, Component.literal(pMessage));
	}

	public static void logBlueLib(@NotNull Component pMessage) {
		String translatedMessage = pMessage.getString();
		BlueLibConstants.LOGGER.log(BaseLogLevel.BLUELIB, translatedMessage);
	}

	// --- Internal helpers ---

	private static @NotNull Boolean shouldLogBlueLib(@NotNull Level pLogLevel, @NotNull Boolean pIsBlueLib) {
		return pLogLevel == BaseLogLevel.ERROR ||
				pLogLevel == BaseLogLevel.WARNING ||
				pLogLevel == BaseLogLevel.BLUELIB ||
				pIsBlueLib && software.bluelib.config.LoggerConfig.isBlueLibLoggingEnabled;
	}

	private static @NotNull Boolean shouldLog(@NotNull Level pLogLevel) {
		return pLogLevel == BaseLogLevel.ERROR ||
				pLogLevel == BaseLogLevel.WARNING ||
				pLogLevel == BaseLogLevel.BLUELIB ||
				software.bluelib.config.LoggerConfig.isLoggingEnabled;
	}

	private static void logBoth(@NotNull Level pLogLevel, @NotNull Component pMessage) {
		String translatedMessage = pMessage.getString();
		BlueLibConstants.LOGGER.log(pLogLevel, translatedMessage);
	}

	private static void logBoth(Level pLogLevel, Component pMessage, Throwable... pThrowable) {
		String translatedMessage = pMessage.getString();
		BlueLibConstants.LOGGER.log(pLogLevel, translatedMessage, pThrowable);
	}
}
