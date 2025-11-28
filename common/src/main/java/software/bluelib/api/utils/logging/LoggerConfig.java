/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class LoggerConfig {

	@NotNull
	protected static final String RESET = "\u001B[0m";

	public static void configureLogger(@NotNull Logger pLogger, @NotNull ILogColorProvider pColorProvider) {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter() {

			@Override
			@NotNull
			public synchronized String format(@NotNull LogRecord pRecord) {
				String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				int color = pColorProvider.getColor(pRecord.getLevel());
				String ansiColor = rgbToAnsi(color);

				StringBuilder coloredMessage = new StringBuilder(ansiColor +
						"[" + timestamp + "]" + " [" + pRecord.getLevel() + "]: " + pRecord.getMessage() + RESET);

				String plainMessage = "[" + timestamp + "]" + " [" + pRecord.getLevel() + "]: " + pRecord.getMessage();

				if (pRecord.getThrown() != null) {
					String exceptionDetails = getExceptionDetails(pRecord);
					coloredMessage.append(exceptionDetails);
					plainMessage += exceptionDetails;
				}

				LogCache.addLog(plainMessage, color);

				return coloredMessage + "\n";
			}

			private static @NotNull String getExceptionDetails(@NotNull LogRecord pRecord) {
				Throwable thrown = pRecord.getThrown();
				StringBuilder exceptionDetails = new StringBuilder("\nException: " + (thrown.getMessage() != null ? thrown.getMessage() : thrown.getClass().getName()));
				for (StackTraceElement element : thrown.getStackTrace()) {
					String fullClassName = element.getClassName();
					String packageName;
					String className;
					int lastDot = fullClassName.lastIndexOf('.');
					if (lastDot >= 0) {
						packageName = fullClassName.substring(0, lastDot);
						className = fullClassName.substring(lastDot + 1);
					} else {
						packageName = "";
						className = fullClassName;
					}
					String methodName = element.getMethodName();
					int lineNumber = element.getLineNumber();

					exceptionDetails.append("\n\tat ");
					if (!packageName.isEmpty()) {
						exceptionDetails.append(packageName).append(".");
					}
					exceptionDetails.append(className).append(".").append(methodName).append("(Line: ").append(lineNumber).append(")");
				}
				return exceptionDetails.toString();
			}
		});

		pLogger.setUseParentHandlers(false);
		pLogger.addHandler(handler);
	}

	@NotNull
	private static String rgbToAnsi(@NotNull Integer pRgb) {
		int red = (pRgb >> 16) & 0xFF;
		int green = (pRgb >> 8) & 0xFF;
		int blue = pRgb & 0xFF;
		return String.format("\u001B[38;2;%d;%d;%dm", red, green, blue);
	}
}
