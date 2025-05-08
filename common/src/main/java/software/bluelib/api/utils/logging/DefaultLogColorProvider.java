// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.logging;

import java.util.logging.Level;

@SuppressWarnings("unused")
public class DefaultLogColorProvider implements ILogColorProvider {

    @Override
    public int getColor(Level pLevel) {
        if (pLevel == BaseLogLevel.ERROR) {
            return 0xFF0000; // Red
        } else if (pLevel == BaseLogLevel.WARNING) {
            return 0xFFA500; // Orange
        } else if (pLevel == BaseLogLevel.INFO) {
            return 0x0000FF; // Blue
        } else if (pLevel == BaseLogLevel.SUCCESS) {
            return 0x00FF00; // Green
        } else if (pLevel == BaseLogLevel.BLUELIB) {
            return 0x00FF00; // Green
        } else {
            return 0xFFFFFF; // Default to white
        }
    }
}
