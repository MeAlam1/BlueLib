// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.logging;

import java.util.logging.Level;
import software.bluelib.interfaces.logging.ILogColorProvider;

@SuppressWarnings("unused")
public class DefaultLogColorProvider implements ILogColorProvider {

    @Override
    public String getColor(Level pLevel) {
        if (pLevel == BaseLogLevel.ERROR) {
            return LoggerConfig.RED;
        } else if (pLevel == BaseLogLevel.WARNING) {
            return LoggerConfig.ORANGE;
        } else if (pLevel == BaseLogLevel.INFO) {
            return LoggerConfig.BLUE;
        } else if (pLevel == BaseLogLevel.SUCCESS) {
            return LoggerConfig.GREEN;
        } else if (pLevel == BaseLogLevel.BLUELIB) {
            return LoggerConfig.GREEN;
        } else {
            return LoggerConfig.RESET;
        }
    }
}
