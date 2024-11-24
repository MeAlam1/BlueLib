// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import software.bluelib.interfaces.logging.ILogColorProvider;

/**
 * A {@code public abstract class} responsible for configuring logging settings,
 * including setting up custom colors for log levels.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #configureLogger(Logger, ILogColorProvider)} - Configures a {@link Logger}
 * to use custom colors for log levels.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class LoggerConfig {

    /**
     * ANSI color codes for console output.
     *
     * @since 1.0.0
     */
    protected static final String RESET = "\u001B[0m";

    /**
     * ANSI color codes for console output.
     *
     * @since 1.0.0
     */
    protected static final String RED = "\u001B[31m";

    /**
     * ANSI color codes for console output.
     *
     * @since 1.0.0
     */
    protected static final String ORANGE = "\u001B[38;5;214m";

    /**
     * ANSI color codes for console output.
     *
     * @since 1.0.0
     */
    protected static final String BLUE = "\u001B[34m";

    /**
     * ANSI color codes for console output.
     *
     * @since 1.0.0
     */
    protected static final String GREEN = "\u001B[38;5;10m";

    /**
     * A {@link Logger} configuration method that sets up a {@link ConsoleHandler}
     * with custom color formatting based on log level using the provided {@link ILogColorProvider}.
     *
     * @param pLogger        {@link Logger} - The logger instance to be configured.
     * @param pColorProvider {@link ILogColorProvider} - Provides color codes for different log levels.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void configureLogger(Logger pLogger, ILogColorProvider pColorProvider) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {

            @Override
            public synchronized String format(LogRecord pRecord) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                StringBuilder coloredMessage = new StringBuilder(pColorProvider.getColor(pRecord.getLevel()) +
                        "[" + timestamp + "]" + " [" + pRecord.getLevel() + "]: " + pRecord.getMessage());

                if (pRecord.getThrown() != null) {
                    coloredMessage.append("\nException: ").append(pRecord.getThrown().getMessage());
                    for (StackTraceElement element : pRecord.getThrown().getStackTrace()) {
                        String packageName = element.getClassName().substring(0, element.getClassName().lastIndexOf('.'));
                        String className = element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1);
                        String methodName = element.getMethodName();
                        int lineNumber = element.getLineNumber();

                        coloredMessage.append("\n\tat ")
                                .append(packageName).append(".")
                                .append(className).append(".")
                                .append(methodName).append("(Line: ")
                                .append(lineNumber).append(")");
                    }
                }

                coloredMessage.append(RESET);
                return coloredMessage + "\n";
            }
        });

        pLogger.setUseParentHandlers(false);
        pLogger.addHandler(handler);
    }
}
