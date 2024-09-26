// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import software.bluelib.interfaces.logging.ILogColorProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A {@code class} responsible for configuring logging settings
 * including setting up custom colors for log levels.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #configureLogger(Logger, ILogColorProvider)} - Configures a {@link Logger} to use custom colors for log levels.</li>
 * </ul>
 * @author MeAlam
 * @since 1.0.0
 */
public abstract class LoggerConfig {

    protected static final String RESET = "\u001B[0m";
    protected static final String RED = "\u001B[31m";
    protected static final String ORANGE = "\u001B[38;5;214m";
    protected static final String BLUE = "\u001B[34m";
    protected static final String GREEN = "\u001B[38;5;10m";

    /**
     * A {@link Logger} configuration method that sets up a {@link ConsoleHandler} with custom color formatting
     * based on log level using the provided {@link ILogColorProvider}.
     *
     * @param pLogger {@link Logger} - The logger instance to be configured.
     * @param pColorProvider {@link ILogColorProvider} - Provides color codes for different log levels.
     */
    public static void configureLogger(Logger pLogger, ILogColorProvider pColorProvider) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
        @Override
        public synchronized String format(LogRecord pRecord) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                StringBuilder coloredMessage = new StringBuilder(pColorProvider.getColor(pRecord.getLevel()) + timestamp + " [" + pRecord.getLevel() + "] " + pRecord.getMessage());

                if (pRecord.getThrown() != null) {
                    coloredMessage.append("\nException: ").append(pRecord.getThrown().getMessage());
                    for (StackTraceElement element : pRecord.getThrown().getStackTrace()) {
                        coloredMessage.append("\n\tat ").append(element.toString());
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
