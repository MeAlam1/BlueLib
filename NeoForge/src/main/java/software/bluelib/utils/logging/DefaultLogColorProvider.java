package software.bluelib.utils.logging;

import software.bluelib.interfaces.logging.ILogColorProvider;

import java.util.logging.Level;

public class DefaultLogColorProvider implements ILogColorProvider {

    @Override
    public String getColor(Level pLevel) {
        if (pLevel == BlueLibLogLevel.ERROR) {
            return LoggerConfig.RED;
        } else if (pLevel == BlueLibLogLevel.WARNING) {
            return LoggerConfig.ORANGE;
        } else if (pLevel == BlueLibLogLevel.INFO) {
            return LoggerConfig.BLUE;
        } else if (pLevel == BlueLibLogLevel.SUCCESS) {
            return LoggerConfig.GREEN;
        } else if (pLevel == BlueLibLogLevel.BLUELIB) {
            return LoggerConfig.GREEN;
        } else {
            return LoggerConfig.RESET;
        }
    }
}
