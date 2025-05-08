// Copyright (c) BlueLib. Licensed under the MIT License.

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

    protected static final String RESET = "\u001B[0m";

    public static void configureLogger(Logger pLogger, ILogColorProvider pColorProvider) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {

            @Override
            public synchronized String format(LogRecord pRecord) {
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

            private static @NotNull String getExceptionDetails(LogRecord pRecord) {
                StringBuilder exceptionDetails = new StringBuilder("\nException: " + pRecord.getThrown().getMessage());
                for (StackTraceElement element : pRecord.getThrown().getStackTrace()) {
                    String packageName = element.getClassName().substring(0, element.getClassName().lastIndexOf('.'));
                    String className = element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1);
                    String methodName = element.getMethodName();
                    int lineNumber = element.getLineNumber();

                    exceptionDetails.append("\n\tat ").append(packageName).append(".").append(className).append(".").append(methodName).append("(Line: ").append(lineNumber).append(")");
                }
                return exceptionDetails.toString();
            }
        });

        pLogger.setUseParentHandlers(false);
        pLogger.addHandler(handler);
    }

    private static String rgbToAnsi(int pRgb) {
        int red = (pRgb >> 16) & 0xFF;
        int green = (pRgb >> 8) & 0xFF;
        int blue = pRgb & 0xFF;
        return String.format("\u001B[38;2;%d;%d;%dm", red, green, blue);
    }
}
