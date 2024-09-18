package software.bluelib.utils.logging;

import software.bluelib.interfaces.logging.ILogColorProvider;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public abstract class LoggerConfig {

    protected static final String RESET = "\u001B[0m";
    protected static final String RED = "\u001B[31m";
    protected static final String ORANGE = "\u001B[38;5;214m";
    protected static final String BLUE = "\u001B[34m";
    protected static final String GREEN = "\u001B[38;5;10m";

    public static void configureLogger(Logger pLogger, ILogColorProvider pColorProvider) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(java.util.logging.LogRecord pRecord) {
                String color = pColorProvider.getColor(pRecord.getLevel());
                return color + pRecord.getLevel().getName() + pRecord.getMessage() + RESET + "\n";
            }
        });
        pLogger.setUseParentHandlers(false);
        pLogger.addHandler(handler);
    }
}
